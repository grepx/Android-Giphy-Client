package gregpearce.gifhub.presenter

import rx.Observable

interface MainPresenter {
    fun getQuery(): String
    fun doSearch(query: String, page: Int): Observable<SearchResultPage>
}