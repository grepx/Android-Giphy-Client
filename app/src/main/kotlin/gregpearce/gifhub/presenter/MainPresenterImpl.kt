package gregpearce.gifhub.presenter

import gregpearce.gifhub.api.GiphyApi
import gregpearce.gifhub.app.GiphyApiKey
import gregpearce.gifhub.util.rx.assert
import gregpearce.gifhub.util.rx.timberd
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainPresenterImpl @Inject constructor() : MainPresenter {
    @Inject lateinit var giphyApi: GiphyApi

    // the default view state: an empty search and an empty set of results
    var searchQuery = ""
    var searchResult = Observable.just(SearchResultPage(0, listOf()))

    /**
     * Maps search terms to search results (by way of a network query + other business logic)
     */
    override fun doSearch(query: String): Observable<SearchResultPage> {
        // if the query parameter has changed, do a new query. Otherwise, reuse the current one.
        if (query != this.searchQuery) {
            this.searchQuery = query
            this.searchResult = giphyApi.search(GiphyApiKey, query)
                    .timberd { "${it.data.size} gifs returned from search." }
                    // assert that the response code is valid
                    .assert({ it.meta.status == 200 },
                            { "Invalid Giphy API response status code: ${it.meta.status}" })
                    // retry 3 times before giving up
                    .retry(3)
                    .map {
                        // map the network model to the view model
                        var urls = it.data.map {
                            Gif(it.images.fixedWidth.url, it.images.fixedWidthStill.url)
                        }
                        SearchResultPage(it.pagination.totalCount, urls)
                    }
                    // cache the result, if there's a config change we can resubmit
                    .cache()
        }
        return searchResult
    }

    override fun getQuery(): String {
        return searchQuery
    }
}