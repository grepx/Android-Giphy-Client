package gregpearce.gifhub.di

import dagger.Component
import gregpearce.gifhub.ui.view.GifDetailActivity
import gregpearce.gifhub.ui.view.GifAdapter
import gregpearce.gifhub.ui.view.MainView

@ViewScope
@Component(
        dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(ViewModule::class)
)
interface ViewComponent {
    fun inject(mainView: MainView)
    fun inject(mainView: GifAdapter)
    fun inject(activity: GifDetailActivity)
}