package gregpearce.gifhub

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.collections.forEachByIndex
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

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
        giphyApi.search(query)
                // subscribe on io thread to prevent blocking main thread
                .subscribeOn(Schedulers.io())
                // observe on main thread to do UI stuff
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    var results = "Search results:"
                    it.url.forEachByIndex {
                        results += it
                        results += "\n"
                    }
                    toast(results)
                }
    }
}