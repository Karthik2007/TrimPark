package com.karthik.trimpark.util

import com.karthik.trimpark.base.util.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 *
 * This class provides the co routine dispatchers option by injecting objects
 * This helps in setting up mock dispatchers for testing coroutine functions
 *
 * created by Karthik A
 */
open class TestCoroutineDispachterProvider: CoroutineDispatcherProvider() {

    override val Main: CoroutineDispatcher by lazy { Dispatchers.Unconfined }
    override val IO: CoroutineDispatcher by lazy { Dispatchers.Unconfined }
}