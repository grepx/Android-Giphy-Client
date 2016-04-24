package gregpearce.gifhub.model

// by having default "empty" values for everything, we can easily initialize an "empty" version of each class
data class SearchResultPage(val totalCount: Int = 0, val gifs: List<Gif> = listOf())

data class SearchMetaData(val totalCount: Int = 0)