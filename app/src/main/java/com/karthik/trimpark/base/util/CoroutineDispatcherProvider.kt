package com.karthik.trimpark.base.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 *
 * This class provides the corountine dispatchers option by injecting MAIN/IO objects
 * This helps in setting up mock dispatchers for testing coroutine functions
 *
 * ref:
 * https://android.jlelse.eu/mastering-coroutines-android-unit-tests-8bc0d082bf15
 *
 * created by Karthik A
 */
open class CoroutineDispatcherProvider {

    open val Main: CoroutineDispatcher by lazy { Dispatchers.Main }
    open val IO: CoroutineDispatcher by lazy { Dispatchers.IO }
}