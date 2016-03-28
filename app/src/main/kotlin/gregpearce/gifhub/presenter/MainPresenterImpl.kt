package gregpearce.gifhub.presenter

import gregpearce.gifhub.api.GiphyApi
import gregpearce.gifhub.api.model.GiphySearchResponse
import gregpearce.gifhub.app.GiphyApiKey
import gregpearce.gifhub.rx.applySchedulers
import gregpearce.gifhub.rx.assert
import gregpearce.gifhub.rx.timberd
import gregpearce.gifhub.view.MainView
import org.jetbrains.anko.toast
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainPresenterImpl @Inject constructor() : MainPresenter {

    @Inject lateinit var giphyApi: GiphyApi

    var view: MainView? = null

    override fun registerView(view: MainView) {
        this.view = view
    }

    override fun unregisterView(view: MainView) {
        this.view = null
    }

    override fun search(query: String) {
        // todo: move this logic out of the presenter
        giphyApi.search(GiphyApiKey, query)
                .timberd { "Giphy API response received." }
                // assert that the response code is valid
                .assert({ it.meta.status == 200 },
                        { "Invalid Giphy API response status code: ${it.meta.status}" })
                // retry 3 times before giving up
                .retry(3)
                // apply the schedulers just before subscribe, so all the above work is done off the UI Thread
                .applySchedulers()
                .subscribe({
                    var urls = it.data.map { it.images.fixedWidth.webp }
                    view?.setGifUrls(urls)
                }, {
                    Timber.e(it, it.message)
                })
    }
}