package gregpearce.gifhub.view

import android.app.Activity
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import gregpearce.gifhub.presenter.MainPresenter
import gregpearce.gifhub.presenter.MainPresenterImpl
import org.jetbrains.anko.*
import timber.log.Timber
import javax.inject.Inject

// todo: all of the view code needs refactoring to listen in on Activity lifecycle events and subscribe/unsubscribe to
// the presenter
class MainUI : AnkoComponent<MainActivity>, MainView {
    @Inject lateinit var activity: Activity
    @Inject lateinit var presenter: MainPresenter

    lateinit var searchEditText: EditText
    lateinit var searchButton: Button

    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
            is EditText -> v.textSize = 24f
        }
    }

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        ui.owner.getComponent().inject(this@MainUI)

        var layout = verticalLayout {
            padding = dip(32)

            searchEditText = editText()

            searchButton = button("Search for gifs...") {
                onClick { searchButtonClick() }
                toast("")
            }
        }.style(customStyle)

        presenter.registerView(this@MainUI)

        return@with layout
    }

    fun searchButtonClick() {
        Timber.d("searchButtonClick")
        presenter.search(searchEditText.text.toString())
    }

    override fun setGifUrls(gifUrls: List<String>) {
        activity.toast("Number of gifs received: ${gifUrls.size}")
    }
}