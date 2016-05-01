package gregpearce.gifhub.app

import android.util.LruCache
import gregpearce.gifhub.api.GiphyApi
import gregpearce.gifhub.api.model.GiphySearchResponse
import gregpearce.gifhub.util.rx.assert
import gregpearce.gifhub.util.rx.timberd
import rx.Observable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GiphySearchCache @Inject constructor() {
    @Inject lateinit var giphyApi: GiphyApi

    var cache = LruCache<Int, Observable<GiphySearchResponse>>(5)
    var lastSearch = ""

    fun getPage(search: String, pageIndex: Int): Observable<GiphySearchResponse> {
        if (lastSearch != search)
            cache.evictAll()

        var page = cache.get(pageIndex)
        if (page == null) {
            page = fetchPage(search, pageIndex)
            cache.put(pageIndex, page)
            Timber.d("Page $pageIndex added to cache, cache size: ${cache.size()}")
        }

        return page
    }

    private fun fetchPage(search: String, pageIndex: Int): Observable<GiphySearchResponse> {
        Timber.d("Fetching page: $pageIndex")

        // calculate offset for this page
        val offset = (pageIndex + GiphyPageStart) * GiphyPageSize

        return giphyApi.search(GiphyApiKey, search, offset, GiphyPageSize)
                .timberd { "${it.data.size} gifs returned from search." }
                // assert that the response code is valid
                .assert({ it.meta.status == 200 },
                        { "Invalid Giphy API response status code: ${it.meta.status}" })
                // retry 3 times before giving up
                .retry(3)
                // cache the result for reuse by later subscribers
                .cache()
    }
}