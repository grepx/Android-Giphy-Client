package gregpearce.gifhub.ui.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import gregpearce.gifhub.app.GiphyPageSize
import gregpearce.gifhub.ui.model.Gif
import gregpearce.gifhub.ui.presenter.MainPresenter
import rx.Observable
import timber.log.Timber

class GifAdapter(val presenter: MainPresenter) : RecyclerView.Adapter<GifViewHolder>() {
    var count = 0

    fun update(count: Int) {
        this.count = count
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GifViewHolder? {
        return GifViewHolder(GifView(parent!!.context))
    }

    override fun getItemCount(): Int {
        return count
    }

    override fun onBindViewHolder(holder: GifViewHolder?, position: Int) {
        holder!!.setModel(getModel(position))
    }

    private fun getModel(position: Int): Observable<Gif> {
        // figure out which page to get
        var pageIndex = position / GiphyPageSize
        var pagePosition = position - pageIndex * GiphyPageSize
        Timber.d("Position: $position, Page: $pageIndex, Page Position: $pagePosition")

        // get the page
        val page = presenter.getSearchResultPage(pageIndex)

        // filter to get the requested gif element within the page
        return page.map { it.gifs.elementAt(pagePosition) }
    }
}