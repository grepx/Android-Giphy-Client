package gregpearce.gifhub.di

import dagger.Component
import gregpearce.gifhub.view.MainActivity
import gregpearce.gifhub.app.MainApplication
import gregpearce.gifhub.presenter.MainPresenter
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: MainApplication)

    // expose the presenter to the sub-graphs
    fun exposeMainPresenter() : MainPresenter
}