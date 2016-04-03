package gregpearce.gifhub.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import gregpearce.gifhub.presenter.MainPresenter
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import timber.log.Timber
import javax.inject.Inject

class MainViewImp : LinearLayout, MainView {
    @Inject lateinit var activity: BaseActivity
    @Inject lateinit var presenter: MainPresenter

    lateinit var searchEditText: EditText
    lateinit var searchButton: Button

    private fun init() {
        (context as BaseActivity).getComponent().inject(this)
        initView()
        presenter.registerView(this)
    }

    private fun initView() = AnkoContext.createDelegate(this).apply {
        padding = dip(32)
        orientation = VERTICAL

        searchEditText = editText()

        searchButton = button("Search for gifs...") {
            onClick { searchButtonClick() }
            toast("")
        }

        style(customStyle)
    }

    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
            is EditText -> v.textSize = 24f
        }
    }

    fun searchButtonClick() {
        Timber.d("searchButtonClick")
        presenter.search(searchEditText.text.toString())
    }

    override fun setGifUrls(gifUrls: List<String>) {
        activity.toast("Number of gifs received: ${gifUrls.size}")
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