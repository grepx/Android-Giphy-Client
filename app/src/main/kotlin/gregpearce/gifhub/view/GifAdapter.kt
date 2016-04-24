package gregpearce.gifhub.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import gregpearce.gifhub.model.Gif

class GifAdapter : RecyclerView.Adapter<GifViewHolder>() {
    var gifs = listOf<Gif>()

    fun setModel(gifs : List<Gif>) {
        this.gifs = gifs
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GifViewHolder? {
        return GifViewHolder(GifView(parent!!.context))
    }

    override fun getItemCount(): Int {
        return gifs.size
    }

    override fun onBindViewHolder(holder: GifViewHolder?, position: Int) {
        holder!!.setModel(gifs[position])
    }
}