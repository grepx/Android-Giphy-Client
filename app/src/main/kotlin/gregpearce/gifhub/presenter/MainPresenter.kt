package gregpearce.gifhub.presenter

import gregpearce.gifhub.app.GiphyApiKey
import gregpearce.gifhub.util.rx.applySchedulers
import gregpearce.gifhub.util.rx.assert
import gregpearce.gifhub.util.rx.timberd
import gregpearce.gifhub.view.MainView
import rx.Observable
import timber.log.Timber

interface MainPresenter {
    fun getQuery(): String
    fun doSearch(query: String): Observable<SearchResultPage>
}