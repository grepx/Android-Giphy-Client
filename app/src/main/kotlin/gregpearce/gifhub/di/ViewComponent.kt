package gregpearce.gifhub.di

import dagger.Component
import gregpearce.gifhub.app.MainApplication
import gregpearce.gifhub.view.MainActivity
import gregpearce.gifhub.view.MainViewImp
import javax.inject.Singleton

@ViewScope
@Component(
        dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(ViewModule::class)
)
interface ViewComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainView: MainViewImp)
}