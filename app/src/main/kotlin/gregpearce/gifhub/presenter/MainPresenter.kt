package gregpearce.gifhub.presenter

import gregpearce.gifhub.app.GiphyApiKey
import gregpearce.gifhub.rx.applySchedulers
import gregpearce.gifhub.rx.assert
import gregpearce.gifhub.rx.timberd
import gregpearce.gifhub.view.MainView
import timber.log.Timber

interface MainPresenter {
    fun registerView(view: MainView)
    fun unregisterView(view: MainView)
    fun search(query: String)
}