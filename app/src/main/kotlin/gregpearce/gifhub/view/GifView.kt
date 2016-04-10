package gregpearce.gifhub.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import gregpearce.gifhub.R
import gregpearce.gifhub.presenter.Gif
import org.jetbrains.anko.*

class GifView : RelativeLayout {

    lateinit var thumbnail: ImageView

    private fun init() {
        initView()
    }

    private fun initView() = AnkoContext.createDelegate(this).apply {
        padding = dip(5)

        thumbnail = imageView {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
        }
    }

    fun setModel(gif: Gif) {
        Glide.with(context).load(gif.thumbnailUrl).placeholder(R.mipmap.ic_launcher).into(thumbnail)
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