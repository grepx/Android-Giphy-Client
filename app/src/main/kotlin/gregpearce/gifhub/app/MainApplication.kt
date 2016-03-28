package gregpearce.gifhub.app

import android.app.Application
import timber.log.Timber

class MainApplication : Application() {

    companion object {
        lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        graph = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        graph.inject(this)
    }
}