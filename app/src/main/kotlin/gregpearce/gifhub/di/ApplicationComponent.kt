package gregpearce.gifhub.di

import com.squareup.sqlbrite.BriteDatabase
import dagger.Component
import gregpearce.gifhub.app.GiphySearchCache
import gregpearce.gifhub.app.MainApplication
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: MainApplication)

    fun exposeGiphySearchCache(): GiphySearchCache
    fun exposeBriteDatabase(): BriteDatabase
}