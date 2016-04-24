package gregpearce.gifhub.view

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import android.view.ViewManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.jakewharton.rxbinding.widget.textChanges
import gregpearce.gifhub.presenter.MainPresenter
import gregpearce.gifhub.util.rx.applySchedulers
import gregpearce.gifhub.util.rx.timberd
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.editText
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.style
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainView : LinearLayout {
    @Inject lateinit var activity: BaseActivity
    @Inject lateinit var presenter: MainPresenter

    lateinit var searchEditText: EditText
    val gifAdapter = GifAdapter()

    private fun init() {
        (context as BaseActivity).getComponent().inject(this)
        initView()
        subscribeToPresenter()
    }

    private fun initView() = AnkoContext.createDelegate(this).apply {
        orientation = VERTICAL

        searchEditText = editText {
            text.insert(0, presenter.getQuery())
            text.insert(0, "cat")
        }

        recyclerView {
            adapter = gifAdapter
            layoutManager = GridLayoutManager(activity, 2)
        }

        val customStyle = { v: Any ->
            when (v) {
                is Button -> v.textSize = 26f
                is EditText -> v.textSize = 24f
            }
        }
        style(customStyle)
    }

    private fun subscribeToPresenter() {
        /* The basic idea:
            - We observe changes to the EditText field
            - flatMap the changes to calls to the presenter search method
            - subscribe to the result and display in the UI
         */
        searchEditText.textChanges()
                // subscribe to the text changes on the UI thread
                .subscribeOn(AndroidSchedulers.mainThread())
                // convert to a string
                .map { it.toString() }
                // wait for 500ms pause between typing characters to prevent spamming the network on every character
                .debounce(500, TimeUnit.MILLISECONDS)

                // perform a search for the search term
                .timberd { "Querying for: $it" }
                .flatMap { presenter.doSearch(it) }

                // apply the default schedulers just before subscribe, so all the above work is done off the UI Thread
                .applySchedulers()
                .subscribe({
                    gifAdapter.setModel(it.gifs)
                }, {
                    Timber.e(it, it.message)
                })
    }

    // todo: see if the constructors can be abstracted out to an interface or something else clever
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.mainViewImp() = mainViewImp {}

inline fun ViewManager.mainViewImp(init: MainView.() -> Unit) = ankoView({ MainView(it) }, init)