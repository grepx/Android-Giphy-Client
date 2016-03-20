package gregpearce.gifhub

import android.app.Application
import android.content.Context
import android.util.Log
import javax.inject.Named
import javax.inject.Singleton

import dagger.Module
import dagger.Provides

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
    fun provideExampleString(): String {
        return "example injection"
    }
}