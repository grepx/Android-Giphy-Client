package gregpearce.gifhub.di

import android.app.Activity
import dagger.Module
import dagger.Provides

/**
 * A module for defining View scope dependencies.
 * These are dependencies that conform to the Activity lifecycle and generally depend on the Activity Context.
 */
@Module
class ViewModule(private val activity: Activity) {
    @Provides
    @ViewScope
    fun provideActivity(): Activity {
        // Keeping the type as Activity rather than Context avoids confusion with the Application Context.
        // Logic that requires a Context should use the Application one where possible.
        return activity
    }
}