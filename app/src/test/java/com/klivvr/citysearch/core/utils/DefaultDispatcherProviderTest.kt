package com.klivvr.citysearch.core.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler

class DefaultDispatcherProviderTest(
    private val scheduler: TestCoroutineScheduler
) : DispatcherProvider {
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher(scheduler)

    override val main: CoroutineDispatcher get() = dispatcher
    override val io: CoroutineDispatcher get() = dispatcher
    override val default: CoroutineDispatcher get() = dispatcher
}