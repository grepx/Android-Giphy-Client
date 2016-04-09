package gregpearce.gifhub.api.model

import com.google.gson.annotations.SerializedName

/**
 * Used by GSON to parse the Giphy Response.
 */
data class GiphySearchResponse(val data: List<GiphyData>, val meta: Meta, val pagination: Pagination) {
    data class GiphyData(val id: String, val images: ImageFormats)

    data class ImageFormats(@SerializedName("fixed_width") val fixedWidth: FixedWidthImage)
    data class FixedWidthImage(val webp: String)

    data class Meta(val status: Int)

    data class Pagination(val totalCount: Int, val count: Int, val offset: Int)
}





