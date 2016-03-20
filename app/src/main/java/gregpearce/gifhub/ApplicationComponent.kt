package gregpearce.gifhub

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: MainApplication)

    fun inject(mainActivity: MainActivity)
}