package gregpearce.gifhub

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import gregpearce.gifhub.api.GiphyApi
import gregpearce.gifhub.rx.applySchedulers
import gregpearce.gifhub.rx.assert
import gregpearce.gifhub.rx.debug
import org.jetbrains.anko.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var giphyApi: GiphyApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainApplication.graph.inject(this)

        verticalLayout {
            val searchQuery = editText()
            button("Search for gifs...") {
                onClick { search(searchQuery.text.toString()) }
            }
        }
    }

    fun search(query: String) {
        giphyApi.search(GiphyApiKey, query)
                // assert that the response code is valid
                .assert({it.meta.status == 200},
                        {"Invalid Giphy API response status code: ${it.meta.status}"})
                // retry 3 times before giving up
                .retry(3)
                .debug { "Received API response, data size: ${it.data.size}" }
                // apply the schedulers just before subscribe, so all the above work is done off the UI Thread
                .applySchedulers()
                .subscribe({
                    toast("Received Giphy API reponse")
                }, {
                    Log.e("GifHubApp", "API failed to respond")
                })
    }
}