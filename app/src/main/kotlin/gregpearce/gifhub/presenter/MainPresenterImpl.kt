package gregpearce.gifhub.presenter

import gregpearce.gifhub.api.GiphyApi
import gregpearce.gifhub.api.model.GiphySearchResponse
import gregpearce.gifhub.app.GiphyApiKey
import gregpearce.gifhub.rx.applySchedulers
import gregpearce.gifhub.rx.assert
import gregpearce.gifhub.rx.timberd
import gregpearce.gifhub.view.MainView
import org.jetbrains.anko.toast
import rx.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainPresenterImpl @Inject constructor() : MainPresenter {
    @Inject lateinit var giphyApi: GiphyApi

    // the default view state: an empty search and an empty set of results
    var searchQuery = ""
    var searchResult = Observable.just(SearchResult(listOf()))

    /**
     * Maps search terms to search results (by way of a network query + other business logic)
     */
    override fun doSearch(query: Observable<String>): Observable<SearchResult> {
        // wait for 500ms pause between typing characters to prevent spamming the network on every character
        return query.debounce(500, TimeUnit.MILLISECONDS)
                .timberd { "Querying for: $it" }
                .flatMap { search(it) }
    }

    private fun search(query: String): Observable<SearchResult> {
        // if the query parameter has changed, do a new query. Otherwise, reuse the current one.
        if (query != this.searchQuery) {
            this.searchQuery = query
            this.searchResult = giphyApi.search(GiphyApiKey, query)
                    .timberd { "Giphy API response received." }
                    // assert that the response code is valid
                    .assert({ it.meta.status == 200 },
                            { "Invalid Giphy API response status code: ${it.meta.status}" })
                    // retry 3 times before giving up
                    .retry(3)
                    .map {
                        // map the network model to the view model
                        var urls = it.data.map { it.images.fixedWidth.webp }
                        SearchResult(urls)
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