package gregpearce.gifhub.di

import dagger.Component
import gregpearce.gifhub.view.GifAdapter
import gregpearce.gifhub.view.MainActivity
import gregpearce.gifhub.view.MainView

@ViewScope
@Component(
        dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(ViewModule::class)
)
interface ViewComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainView: MainView)
    fun inject(mainView: GifAdapter)
}