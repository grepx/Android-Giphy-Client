package gregpearce.gifhub

import rx.Observable
import java.util.concurrent.TimeUnit

class GiphyApi {
    fun search(query: String): Observable<GiphySearchResponse> {
        // sample data
        var urls = listOf(
                "gif 1",
                "gif 2",
                "gif 3",
                "gif 4",
                "gif 5"
        )
        // add a delay to show async behavior
        return Observable.just(GiphySearchResponse(urls)).delay(2, TimeUnit.SECONDS)
    }
}