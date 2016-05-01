package gregpearce.gifhub.ui.presenter

import gregpearce.gifhub.api.GiphyApi
import gregpearce.gifhub.api.model.GiphySearchResponse
import gregpearce.gifhub.app.GiphyApiKey
import gregpearce.gifhub.app.GiphyPageSize
import gregpearce.gifhub.app.GiphyPageStart
import gregpearce.gifhub.di.ViewScope
import gregpearce.gifhub.ui.model.Gif
import gregpearce.gifhub.ui.model.SearchMetaData
import gregpearce.gifhub.ui.model.SearchResultPage
import gregpearce.gifhub.util.rx.assert
import gregpearce.gifhub.util.rx.timberd
import rx.Observable
import rx.lang.kotlin.PublishSubject
import javax.inject.Inject

@ViewScope
class SearchPresenter @Inject constructor() {
    @Inject lateinit var giphyApi: GiphyApi

    var querySubject = PublishSubject<String>()
    var query = querySubject.asObservable()

    fun subscribe(query: Observable<String>) {
        query.subscribe(querySubject)
    }

    fun getResults(): Observable<SearchMetaData> {
        return getResultPage(0).map { SearchMetaData(it.totalCount) }
    }

    fun getResultPage(page: Int): Observable<SearchResultPage> {
        return query.flatMap { search(it, page) }
                .map {
                    // map the network model to the view model
                    val urls = it.data.map {
                        Gif(it.images.fixedWidth.webp, it.images.fixedWidthStill.url)
                    }
                    SearchResultPage(it.pagination.totalCount, urls)
                }
    }

    fun search(query: String, page: Int): Observable<GiphySearchResponse>? {
        // calculate offset for this page
        val offset = (page + GiphyPageStart) * GiphyPageSize

        return giphyApi.search(GiphyApiKey, query, offset, GiphyPageSize)
                .timberd { "${it.data.size} gifs returned from search." }
                // assert that the response code is valid
                .assert({ it.meta.status == 200 },
                        { "Invalid Giphy API response status code: ${it.meta.status}" })
                // retry 3 times before giving up
                .retry(3)
    }
}