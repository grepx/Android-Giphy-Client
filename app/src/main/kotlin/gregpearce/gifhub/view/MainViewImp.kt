package gregpearce.gifhub.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.jakewharton.rxbinding.widget.textChanges
import gregpearce.gifhub.presenter.MainPresenter
import gregpearce.gifhub.rx.addToComposite
import gregpearce.gifhub.rx.applySchedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

class MainViewImp : LinearLayout, MainView {
    @Inject lateinit var activity: BaseActivity
    @Inject lateinit var presenter: MainPresenter

    val compositeSubscription = CompositeSubscription()

    lateinit var searchEditText: EditText
    lateinit var searchResults: TextView

    private fun init() {
        (context as BaseActivity).getComponent().inject(this)
        initView()
        subscribeToPresenter()
    }

    private fun initView() = AnkoContext.createDelegate(this).apply {
        padding = dip(32)
        orientation = VERTICAL

        searchEditText = editText {
            text.insert(0, presenter.getQuery())
        }

        searchEditText.textChanges()

        searchResults = textView {
            text = "Perform a search to view results here"
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
        val searchTerm =
                searchEditText.textChanges().subscribeOn(AndroidSchedulers.mainThread())
                        .map { it.toString() }
                        .filter { !it.isEmpty() }

        presenter.doSearch(searchTerm)
                // apply the default schedulers just before subscribe, so all the above work is done off the UI Thread
                .applySchedulers()
                .subscribe({
                    val resultsText = it.urls.reduceRight { text, url -> "$text\n$url" }
                    searchResults.text = resultsText
                }, {
                    Timber.e(it, it.message)
                })
                .addToComposite(compositeSubscription)
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

inline fun ViewManager.mainViewImp(init: MainViewImp.() -> Unit) = ankoView({ MainViewImp(it) }, init)