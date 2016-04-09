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

    /**
     * Maps search terms to search results (by way of a network query + other business logic)
     */
    override fun searchResults(query: Observable<String>): Observable<SearchResults> {
        var results = query
                // wait for 500ms pause between typing characters to prevent spamming the network on every character
                .debounce(500, TimeUnit.MILLISECONDS)
                .timberd { "Querying for: $it" }
                .flatMap { search(it) }
                .map {
                    // map the network model to the view model
                    var urls = it.data.map { it.images.fixedWidth.webp }
                    SearchResults(urls)
                }
        return results
    }

    fun search(query: String): Observable<GiphySearchResponse> {
        // todo: move this logic out of the presenter
        return giphyApi.search(GiphyApiKey, query)
                .timberd { "Giphy API response received." }
                // assert that the response code is valid
                .assert({ it.meta.status == 200 },
                        { "Invalid Giphy API response status code: ${it.meta.status}" })
                // retry 3 times before giving up
                .retry(3)
    }
}