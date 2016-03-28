package gregpearce.gifhub.app

import dagger.Component
import gregpearce.gifhub.view.MainActivity
import gregpearce.gifhub.app.MainApplication
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: MainApplication)

    fun inject(mainActivity: MainActivity)
}