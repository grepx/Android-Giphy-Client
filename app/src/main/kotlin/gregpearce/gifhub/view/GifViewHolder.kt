package gregpearce.gifhub.view

import android.support.v7.widget.RecyclerView
import gregpearce.gifhub.presenter.Gif

class GifViewHolder(val view: GifView) : RecyclerView.ViewHolder(view) {
    fun setModel(gif: Gif) {
        view.setModel(gif)
    }
}