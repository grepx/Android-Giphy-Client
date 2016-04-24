package gregpearce.gifhub.ui.view

import android.support.v7.widget.RecyclerView
import gregpearce.gifhub.ui.model.Gif
import rx.Observable

class GifViewHolder(val view: GifView) : RecyclerView.ViewHolder(view) {
    fun setModel(gif: Observable<Gif>) {
        view.setModel(gif)
    }
}