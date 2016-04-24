package gregpearce.gifhub.ui.view

import android.os.Bundle
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.setContentView

class GifDetailActivity : BaseActivity() {

    companion object {
        val INTENT_EXTRA_URL = "INTENT_EXTRA_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RootUI().setContentView(this)
    }

    fun getUrl(): String {
        return intent.getStringExtra(INTENT_EXTRA_URL)!!
    }

    private class RootUI : AnkoComponent<GifDetailActivity> {
        override fun createView(ui: AnkoContext<GifDetailActivity>) = with(ui) {
            gifDetailView()
        }
    }
}

