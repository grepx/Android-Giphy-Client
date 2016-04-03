package gregpearce.gifhub.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.ViewManager
import android.widget.Button
import android.widget.EditText
import gregpearce.gifhub.api.GiphyApi
import gregpearce.gifhub.app.GiphyApiKey
import gregpearce.gifhub.app.MainApplication
import gregpearce.gifhub.di.DaggerViewComponent
import gregpearce.gifhub.di.HasComponent
import gregpearce.gifhub.di.ViewComponent
import gregpearce.gifhub.di.ViewModule
import gregpearce.gifhub.rx.applySchedulers
import gregpearce.gifhub.rx.assert
import gregpearce.gifhub.rx.debug
import gregpearce.gifhub.rx.timberd
import org.jetbrains.anko.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent().inject(this)
        RootUI().setContentView(this)
    }

    class RootUI : AnkoComponent<MainActivity> {
        override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
            mainViewImp()
        }
    }
}

