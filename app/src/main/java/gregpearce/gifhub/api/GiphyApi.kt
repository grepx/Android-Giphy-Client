package gregpearce.gifhub.api

import gregpearce.gifhub.api.model.GiphySearchResponse
import retrofit.http.GET
import retrofit.http.Query
import rx.Observable

/**
 * http://api.giphy.com/v1/gifs
 */
interface  GiphyApi {
    @GET("search")
    fun search(@Query("api_key") apiKey: String, @Query("q") query: String) : Observable<GiphySearchResponse>
}