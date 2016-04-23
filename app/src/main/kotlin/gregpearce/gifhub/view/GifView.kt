package gregpearce.gifhub.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import gregpearce.gifhub.R
import gregpearce.gifhub.presenter.Gif
import gregpearce.gifhub.util.glide.listener
import org.jetbrains.anko.*

class GifView : RelativeLayout {
    private enum class ViewState { LoadingSpinner, Thumbnail, Error }

    lateinit var thumbnail: ImageView
    lateinit var loadingSpinner: ProgressBar

    private fun init() {
        initView()
    }

    private fun initView() = AnkoContext.createDelegate(this).apply {
        padding = dip(5)
        layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)

        thumbnail = imageView {
            layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        loadingSpinner = progressBar {
            layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
        }
    }

    fun setModel(gif: Gif) {
        showView(ViewState.LoadingSpinner)
        Glide.with(context).load(gif.thumbnailUrl)
                .listener({
                    showView(ViewState.Thumbnail)
                }, {
                    showView(ViewState.Error)
                })
                .into(thumbnail)
    }

    private fun showView(viewState: ViewState) {
        if (viewState == ViewState.LoadingSpinner) {
            loadingSpinner.visibility = VISIBLE
            thumbnail.visibility = INVISIBLE
        } else {
            loadingSpinner.visibility = GONE
            thumbnail.visibility = VISIBLE
        }
        if (viewState == ViewState.Error)
            thumbnail.imageResource = R.mipmap.ic_launcher // todo: get a better error image
    }

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