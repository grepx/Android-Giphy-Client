package gregpearce.gifhub.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import gregpearce.gifhub.api.GiphyApi
import gregpearce.gifhub.app.GiphyApiUrl
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import javax.inject.Singleton

/**
 * A module for defining Application scope dependencies.
 * These are usually dependencies that live in memory for the duration of the app's lifetime (Singletons).
 */
@Module
class ApplicationModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideGiphyApi(): GiphyApi {
        val retrofit = Retrofit.Builder()
                .baseUrl(GiphyApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(GiphyApi::class.java)
    }
}