package gregpearce.gifhub.view

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding.widget.textChanges
import gregpearce.gifhub.R
import gregpearce.gifhub.presenter.MainPresenter
import gregpearce.gifhub.util.rx.addToComposite
import gregpearce.gifhub.util.rx.applySchedulers
import gregpearce.gifhub.util.rx.timberd
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewImp : LinearLayout, MainView {
    @Inject lateinit var activity: BaseActivity
    @Inject lateinit var presenter: MainPresenter

    lateinit var searchEditText: EditText
    lateinit var searchResults: LinearLayout

    private fun init() {
        (context as BaseActivity).getComponent().inject(this)
        initView()
        subscribeToPresenter()
    }

    private fun initView() = AnkoContext.createDelegate(this).apply {
        padding = dip(5)
        orientation = VERTICAL

        searchEditText = editText {
            text.insert(0, presenter.getQuery())
        }

        searchEditText.textChanges()

        scrollView {
            layoutDirection = VERTICAL
            searchResults = linearLayout {
                orientation = VERTICAL
            }
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
                    searchResults.removeAllViews()
                    it.urls.forEach {
                        var imageView = ImageView(activity)
                        Glide.with(activity).load(it).placeholder(R.mipmap.ic_launcher).into(imageView)
                        searchResults.addView(imageView)
                    }
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

inline fun ViewManager.mainViewImp(init: MainViewImp.() -> Unit) = ankoView({ MainViewImp(it) }, init)