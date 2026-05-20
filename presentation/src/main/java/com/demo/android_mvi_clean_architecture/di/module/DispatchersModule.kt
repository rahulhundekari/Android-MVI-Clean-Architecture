package com.demo.android_mvi_clean_architecture.di.module

import com.demo.android_mvi_clean_architecture.di.DefaultDispatcher
import com.demo.android_mvi_clean_architecture.di.IoDispatcher
import com.demo.android_mvi_clean_architecture.di.MainDispatcher
import com.demo.data.utils.DispatchersProviderImpl
import com.demo.domain.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class DispatchersModule {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): MainCoroutineDispatcher = Dispatchers.Main

    @Provides
    fun providesDispatcherProvider(
        @IoDispatcher io: CoroutineDispatcher,
        @DefaultDispatcher default: CoroutineDispatcher,
        @MainDispatcher main: MainCoroutineDispatcher
    ): DispatcherProvider {
        return DispatchersProviderImpl(
            io = io,
            default = default,
            main = main
        )
    }
}