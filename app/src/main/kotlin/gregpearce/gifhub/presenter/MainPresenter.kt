package gregpearce.gifhub.presenter

import gregpearce.gifhub.model.SearchMetaData
import gregpearce.gifhub.model.SearchResultPage
import rx.Observable

interface MainPresenter {
    fun setQuery(query: String)
    fun getQuery(): String
    fun getSearchMetaData() : Observable<SearchMetaData>
    fun getSearchResultPage(pageIndex: Int): Observable<SearchResultPage>
}