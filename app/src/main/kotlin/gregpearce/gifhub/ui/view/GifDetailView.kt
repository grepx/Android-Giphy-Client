package gregpearce.gifhub.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewManager
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ProgressBar
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.webView
import timber.log.Timber

class GifDetailView : FrameLayout {

    lateinit var gifWebView: WebView

    init {
        initView()
        val url = (context as GifDetailActivity).getUrl()
        Timber.d("Loading gif url: $url")

        gifWebView.loadUrl(url)
        gifWebView.loadDataWithBaseURL(url, "<html><img style='width:100%' src='$url' /></html>", null, null, null)
    }

    private fun initView() = AnkoContext.createDelegate(this).apply {
        gifWebView = webView {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
        }
    }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.gifDetailView() = gifDetailView {}

inline fun ViewManager.gifDetailView(init: GifDetailView.() -> Unit) = ankoView({ GifDetailView(it) }, init)