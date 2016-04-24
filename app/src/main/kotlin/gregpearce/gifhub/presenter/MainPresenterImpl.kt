package gregpearce.gifhub.presenter

import gregpearce.gifhub.api.GiphyApi
import gregpearce.gifhub.app.GiphyApiKey
import gregpearce.gifhub.util.rx.assert
import gregpearce.gifhub.util.rx.timberd
import rx.Observable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainPresenterImpl @Inject constructor() : MainPresenter {
    @Inject lateinit var giphyApi: GiphyApi

    // the default view state: an empty search and an empty set of results
    var searchQuery = ""

    var searchPageCache = WeakHashMap<Int, Observable<SearchResultPage>>()

    /**
     * Maps search terms to search results (by way of a network query + other business logic)
     */
    override fun doSearch(query: String, page: Int): Observable<SearchResultPage> {
        // if the query parameter has changed, clean out the page cache and start again
        if (query != this.searchQuery) {
            searchPageCache.clear()
            this.searchQuery = query
        }
        // see if we already have this page cached
        var searchResultPage = searchPageCache.get(page)
        // if not, construct and store it
        if (searchResultPage == null) {
            // check to see if this page is inside the page cache
            searchResultPage = giphyApi.search(GiphyApiKey, query)
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
                    // cache the result, if there's a config change, or multiple subscribers in the UI, it is reused
                    .cache()
            searchPageCache.put(page, searchResultPage)
        }
        return searchResultPage!!
    }

    override fun getQuery(): String {
        return searchQuery
    }
}