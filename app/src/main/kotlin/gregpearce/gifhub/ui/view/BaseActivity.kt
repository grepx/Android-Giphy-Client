package gregpearce.gifhub.ui.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import gregpearce.gifhub.app.MainApplication
import gregpearce.gifhub.di.DaggerViewComponent
import gregpearce.gifhub.di.HasComponent
import gregpearce.gifhub.di.ViewComponent
import gregpearce.gifhub.di.ViewModule

/**
 * A base class that contains logic common to all activities, such as the Retained Fragment that manages Presenters.
 * It also exposes a common interface to all views for DI.
 */
abstract class BaseActivity : AppCompatActivity(), HasComponent {
    lateinit var viewComponent: ViewComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewComponent = DaggerViewComponent.builder()
                .applicationComponent(MainApplication.graph)
                .viewModule(ViewModule(this))
                .build()
    }

    override fun getComponent(): ViewComponent {
        return viewComponent
    }
}