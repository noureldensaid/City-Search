package com.klivvr.citysearch.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Provides an abstraction over the standard `Dispatchers` to make coroutine dispatchers
 * injectable and replaceable in tests. This allows for controlling the execution of
 * coroutines during testing.
 * DispatcherProvider: This follows Google's official Android architecture recommendations for testable coroutines (see: https://developer.android.com/kotlin/coroutines/coroutines-best-practices)
 */
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

class DefaultDispatcherProvider : DispatcherProvider {

    override val main: CoroutineDispatcher get() = Dispatchers.Main

    override val io: CoroutineDispatcher get() = Dispatchers.IO

    override val default: CoroutineDispatcher get() = Dispatchers.Default
}