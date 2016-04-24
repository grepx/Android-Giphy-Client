package gregpearce.gifhub.presenter

// by having default "empty" values for everything, we can easily initialize an "empty" SearchResultsPage
data class SearchResultPage(val totalCount: Int = 0, val gifs: List<Gif> = listOf())