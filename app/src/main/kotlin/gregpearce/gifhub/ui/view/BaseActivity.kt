package gregpearce.gifhub.ui.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import gregpearce.gifhub.app.MainApplication
import gregpearce.gifhub.di.DaggerViewComponent
import gregpearce.gifhub.di.ViewComponent
import gregpearce.gifhub.di.ViewModule
import gregpearce.gifhub.ui.util.InstanceStateManager
import javax.inject.Inject

/**
 * A base class that contains logic common to all activities, such as the Retained Fragment that manages Presenters.
 * It also exposes a common interface to all views for DI.
 */
abstract class BaseActivity : AppCompatActivity() {
    lateinit var viewComponent: ViewComponent

    @Inject lateinit var instanceStateManager: InstanceStateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModule = ViewModule(this, savedInstanceState ?: Bundle.EMPTY)

        viewComponent = DaggerViewComponent.builder()
                .applicationComponent(MainApplication.graph)
                .viewModule(viewModule)
                .build()

        viewComponent.inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        instanceStateManager.runSaveLambdas(outState!!)
    }
}