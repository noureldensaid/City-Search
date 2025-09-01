package com.klivvr.citysearch.di

import com.klivvr.citysearch.core.utils.DefaultDispatcherProvider
import com.klivvr.citysearch.core.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Singleton
    @Provides
    fun provideDispatchersProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }
}