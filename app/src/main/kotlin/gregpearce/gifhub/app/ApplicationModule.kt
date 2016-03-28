package gregpearce.gifhub.app

import android.app.Application
import android.content.Context
import android.util.Log
import javax.inject.Named
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import gregpearce.gifhub.api.GiphyApi
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory

/**
 * A module for Android-specific dependencies which require a [android.content.Context] or [ ] to create.
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