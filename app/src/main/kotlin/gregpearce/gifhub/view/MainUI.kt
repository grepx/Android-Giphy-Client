package gregpearce.gifhub.view

import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import org.jetbrains.anko.*
import timber.log.Timber

class MainUI : AnkoComponent<MainActivity>, MainView {

    // use the activity as a presenter for now
    lateinit var presenter: MainActivity
    lateinit var searchEditText: EditText
    lateinit var searchButton: Button

    private val customStyle = { v: Any ->
        when (v) {
            is Button -> v.textSize = 26f
            is EditText -> v.textSize = 24f
        }
    }

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        presenter = ui.owner

        verticalLayout {
            padding = dip(32)

            searchEditText = editText()

            searchButton = button("Search for gifs...") {
                onClick { searchButtonClick() }
            }
        }.style(customStyle)
    }

    fun searchButtonClick() {
        Timber.d("searchButtonClick")
        presenter.search(searchEditText.text.toString())
    }
}