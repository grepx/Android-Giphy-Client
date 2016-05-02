package gregpearce.gifhub.ui.presenter

import com.squareup.sqlbrite.BriteDatabase
import gregpearce.gifhub.app.GiphySearchCache
import gregpearce.gifhub.di.ViewScope
import gregpearce.gifhub.ui.model.Gif
import gregpearce.gifhub.ui.model.SearchMetaData
import gregpearce.gifhub.ui.model.SearchResultPage
import rx.Observable
import rx.lang.kotlin.PublishSubject
import javax.inject.Inject

@ViewScope
class SearchPresenter @Inject constructor() {
    @Inject lateinit var giphySearchCache: GiphySearchCache
    @Inject lateinit var db: BriteDatabase

    var querySubject = PublishSubject<String>()
    var query = querySubject.asObservable().replay(1).autoConnect()

    fun subscribe(query: Observable<String>) {
        query.subscribe(querySubject)
    }

    fun getResults(): Observable<SearchMetaData> {
        return getResultPage(0).map { SearchMetaData(it.totalCount) }
    }

    fun getResultPage(page: Int): Observable<SearchResultPage> {
        return query.flatMap { giphySearchCache.getPage(it, page) }
                .map {
                    // map the network model to the view model
                    val urls = it.data.map {
                        Gif(it.images.fixedWidth.webp, it.images.fixedWidthStill.url)
                    }
                    SearchResultPage(it.pagination.totalCount, urls)
                }
    }
}