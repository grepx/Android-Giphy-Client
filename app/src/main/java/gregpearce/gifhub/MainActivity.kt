package gregpearce.gifhub

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.*
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var exampleString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainApplication.graph.inject(this)

        verticalLayout {
            val name = editText()
            button(exampleString) {
                onClick { toast("Hello, ${name.text}!") }
            }
        }
    }
}