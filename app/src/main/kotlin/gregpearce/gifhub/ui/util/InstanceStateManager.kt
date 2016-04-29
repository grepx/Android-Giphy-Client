package gregpearce.gifhub.ui.util

import android.os.Bundle
import gregpearce.gifhub.di.ViewScope
import javax.inject.Inject

@ViewScope
class InstanceStateManager @Inject constructor() {

    @Inject lateinit var savedState: Bundle

    private var outState: Bundle? = null
    private val saveLambdas = arrayListOf<(Bundle) -> Unit>()

    /**
     *  Register a lambda to run and save state when the activity is closing.
     */
    fun registerSaveLambda(save: (outState: Bundle) -> Unit) {
        saveLambdas.add(save)
    }

    /**
     * Run the save lambdas against the out state bundle.
     */
    fun runSaveLambdas(outState: Bundle) {
        this.outState = outState
        saveLambdas.forEach { it(outState) }
    }
}