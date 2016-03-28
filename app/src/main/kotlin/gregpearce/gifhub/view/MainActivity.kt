package gregpearce.gifhub.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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

class MainActivity : AppCompatActivity(), HasComponent {
    lateinit var viewComponent: ViewComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewComponent = DaggerViewComponent.builder()
                .applicationComponent(MainApplication.graph)
                .viewModule(ViewModule(this))
                .build()

        viewComponent.inject(this)
        MainUI().setContentView(this)
    }

    override fun getComponent(): ViewComponent {
        return viewComponent
    }
}