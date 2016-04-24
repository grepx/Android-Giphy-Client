package gregpearce.gifhub.presenter

import gregpearce.gifhub.api.GiphyApi
import gregpearce.gifhub.app.GiphyApiKey
import gregpearce.gifhub.model.Gif
import gregpearce.gifhub.model.SearchMetaData
import gregpearce.gifhub.model.SearchResultPage
import gregpearce.gifhub.util.rx.assert
import gregpearce.gifhub.util.rx.timberd
import rx.Observable
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainPresenterImpl @Inject constructor() : MainPresenter {
    @Inject lateinit var giphyApi: GiphyApi

    // the default view state: an empty search and an empty set of results
    var searchQuery = ""
    lateinit var searchMetaDataCache: Observable<SearchMetaData>
    var searchResultsCache = WeakHashMap<Int, Observable<SearchResultPage>>()

    override fun getSearchMetaData(): Observable<SearchMetaData> {
        if (searchMetaDataCache == null) {
            // get the first page of the results to query for the meta data
            // keep a reference to the result to keep it alive outside of just the weak reference cache
            searchMetaDataCache = getSearchResultPage(0).map { SearchMetaData(it.totalCount) }
        }
        return searchMetaDataCache
    }

    override fun getSearchResultPage(pageIndex: Int): Observable<SearchResultPage> {
        // see if we already have this page cached
        var searchResultPage = searchResultsCache.get(pageIndex)
        // if not, construct and store it
        if (searchResultPage == null) {
            // check to see if this page is inside the page cache
            searchResultPage = giphyApi.search(GiphyApiKey, searchQuery)
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

            Timber.d("Storing page $pageIndex, searchResultsCache now holds ${searchResultsCache.size} pages")
            searchResultsCache.put(pageIndex, searchResultPage)
        }
        return searchResultPage!!
    }

    override fun setQuery(query: String) {
        // if the query parameter has changed, clean out the page cache and start again
        if (query != this.searchQuery) {
            searchResultsCache.clear()
            this.searchQuery = query
        }
    }

    override fun getQuery(): String {
        return searchQuery
    }
}