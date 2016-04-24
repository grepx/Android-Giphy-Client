package gregpearce.gifhub.view

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import android.view.ViewManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.jakewharton.rxbinding.widget.textChanges
import gregpearce.gifhub.presenter.MainPresenter
import gregpearce.gifhub.util.rx.applySchedulers
import gregpearce.gifhub.util.rx.timberd
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import rx.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainView : LinearLayout {
    @Inject lateinit var activity: BaseActivity
    @Inject lateinit var presenter: MainPresenter

    lateinit var searchEditText: EditText
    lateinit var resultsCountTextView: TextView
    val gifAdapter = GifAdapter()

    private fun init() {
        (context as BaseActivity).getComponent().inject(this)
        initView()
        setupViewModel()
    }

    private fun initView() = AnkoContext.createDelegate(this).apply {
        orientation = VERTICAL

        searchEditText = editText {
            text.insert(0, presenter.getQuery())
            text.insert(0, "cat")
        }

        resultsCountTextView = textView {
            padding = dip(5)
            textSize = 15f
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

    private fun setupViewModel() {
        /* The basic idea:
            - We observe changes to the EditText field
            - flatMap the changes to calls to the presenter search method
            - subscribe to the result and display in the UI
         */
        val searchResultViewModel = searchEditText.textChanges()
                // subscribe to the text changes on the UI thread
                .subscribeOn(AndroidSchedulers.mainThread())
                // convert to a string
                .map { it.toString() }
                // wait for 500ms pause between typing characters to prevent spamming the network on every character
                .debounce(500, TimeUnit.MILLISECONDS)
                // filter out duplicates
                .distinctUntilChanged()

                // perform a search for the search term
                .timberd { "Querying for: $it" }
                // get page 0
                .flatMap {
                    presenter.setQuery(it)
                    presenter.getSearchResultPage(0)
                }

                // apply the default schedulers just before subscribe, so all the above work is done off the UI Thread
                .applySchedulers()

        searchResultViewModel.subscribe({
            showResultsCount(it.totalCount)
            gifAdapter.setModel(it.gifs)
        }, {
            Timber.e(it, it.message)
        })
    }

    private fun showResultsCount(count: Int) {
        if (count == 0) {
            resultsCountTextView.text = "No gifs found"
        } else if (count == 1) {
            resultsCountTextView.text = "1 gif found"
        } else {
            resultsCountTextView.text = "$count gifs found"
        }
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
inline fun ViewManager.mainView() = mainView {}

inline fun ViewManager.mainView(init: MainView.() -> Unit) = ankoView({ MainView(it) }, init)