package gregpearce.gifhub.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import gregpearce.gifhub.R
import gregpearce.gifhub.model.Gif
import gregpearce.gifhub.util.glide.listener
import gregpearce.gifhub.util.rx.addToComposite
import gregpearce.gifhub.util.rx.applyDefaults
import org.jetbrains.anko.*
import rx.Observable
import rx.subscriptions.CompositeSubscription
import timber.log.Timber

class GifView : RelativeLayout {
    private enum class ViewState { LoadingSpinner, Thumbnail, Error }

    lateinit var thumbnail: ImageView
    lateinit var loadingSpinner: ProgressBar

    // used to track and unsubscribe previous model subscriptions on view holder rebinding
    var subscription = CompositeSubscription()

    private fun init() {
        initView()
    }

    private fun initView() = AnkoContext.createDelegate(this).apply {
        padding = dip(5)
        minimumHeight = dip(150)
        layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)

        thumbnail = getThumbnailImageView()

        loadingSpinner = progressBar {
            layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
        }
    }

    /**
     * We need to create a new ImageView each time since otherwise it can cause errors and side effects as Glide
     * loads a new image and attempts to adjust the size etc.
     */
    private fun getThumbnailImageView(): ImageView = imageView {
        layoutParams = ViewGroup.LayoutParams(matchParent, wrapContent)
        scaleType = ImageView.ScaleType.FIT_CENTER
    }


    fun setModel(gif: Observable<Gif>) {
        showView(ViewState.LoadingSpinner)
        // unsubscribe the previous subscription
        subscription.clear()

        gif.applyDefaults()
                .subscribe ({
                    displayGif(it)
                }, {
                    Timber.e(it, it.message)
                }).addToComposite(subscription)
    }

    private fun displayGif(gif: Gif) {
        // clear any old unfinished Glide loads from the image view
        Glide.clear(thumbnail)
        thumbnail = getThumbnailImageView()
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