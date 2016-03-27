package gregpearce.gifhub.api.model

import com.google.gson.annotations.SerializedName

/**
 * Used by GSON to parse the Giphy Response.
 */
class GiphySearchResponse(val data: List<GiphyData>, val meta: Meta, val pagination: Pagination) {
    class GiphyData(val id: String, val images: ImageFormats)

    class ImageFormats(@SerializedName("fixed_width") val fixedWidth: FixedWidthImage)
    class FixedWidthImage(val webp: String)

    class Meta(val status: Int)

    class Pagination(val totalCount: Int, val count: Int, val offset: Int)
}





