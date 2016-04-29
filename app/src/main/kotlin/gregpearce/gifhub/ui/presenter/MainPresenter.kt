package gregpearce.gifhub.ui.presenter

import gregpearce.gifhub.ui.model.SearchMetaData
import gregpearce.gifhub.ui.model.SearchResultPage
import rx.Observable

interface MainPresenter {
    fun setQuery(query: String)
    fun getSearchMetaData() : Observable<SearchMetaData>
    fun getSearchResultPage(pageIndex: Int): Observable<SearchResultPage>
}