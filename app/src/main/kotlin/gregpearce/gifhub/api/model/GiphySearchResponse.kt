package gregpearce.gifhub.api.model

import com.google.gson.annotations.SerializedName

/**
 * Used by GSON to parse the Giphy Response.
 */
data class GiphySearchResponse(val data: List<GiphyData>, val meta: Meta, val pagination: Pagination) {
    data class GiphyData(val id: String, val images: ImageFormats)

    data class ImageFormats(@SerializedName("fixed_width") val fixedWidth: GiphyImage,
                            @SerializedName("fixed_width_still") val fixedWidthStill: GiphyImage)
    data class GiphyImage(val url: String, val width: Int, val height: Int)

    data class Meta(val status: Int)

    data class Pagination(@SerializedName("total_count") val totalCount: Int, val count: Int, val offset: Int)
}





