package gregpearce.gifhub.util.glide

import com.bumptech.glide.DrawableRequestBuilder
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.target.Target

/**
 * Clean up the ugly anonymous class of the Glide API.
 */
fun DrawableRequestBuilder<String>.listener(onResourceReady: () -> Unit, onException: () -> Unit): DrawableRequestBuilder<String> {
    return this.listener(object : com.bumptech.glide.request.RequestListener<String, GlideDrawable> {
        override fun onResourceReady(resource: GlideDrawable?, model: String?, target: Target<GlideDrawable>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
            onResourceReady()
            // return false to tell glide to set the drawable onto the ImageView
            return false
        }

        override fun onException(e: Exception?, model: String?, target: Target<GlideDrawable>?, isFirstResource: Boolean): Boolean {
            onException()
            // return true to tell glide we handled the exception
            return true
        }
    })
}