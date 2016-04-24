package gregpearce.gifhub.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import gregpearce.gifhub.R
import gregpearce.gifhub.util.glide.listener
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import timber.log.Timber

class GifDetailView : FrameLayout {

    lateinit var gifImageView: ImageView
    lateinit var loadingSpinner: ProgressBar

    init {
        initView()
        val url = (context as GifDetailActivity).getUrl()
        Timber.d("Loading gif url: $url")

        // todo: it's dumb that I need to cast this back to context...
        Glide.with(context as Context).load(url)
                .listener({
                    loadingSpinner.visibility = GONE
                }, {
                    gifImageView.setImageResource(R.mipmap.ic_launcher)
                })
                .into(gifImageView)
    }

    private fun initView() = AnkoContext.createDelegate(this).apply {
        gifImageView = imageView {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            maxWidth = dip(300)
            maxHeight = dip(300)
        }
        loadingSpinner = progressBar {  }
    }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.gifDetailView() = gifDetailView {}

inline fun ViewManager.gifDetailView(init: GifDetailView.() -> Unit) = ankoView({ GifDetailView(it) }, init)