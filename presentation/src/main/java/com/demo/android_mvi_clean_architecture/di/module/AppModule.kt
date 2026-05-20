package com.demo.android_mvi_clean_architecture.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.demo.android_mvi_clean_architecture.di.AppSettingsSharedPreference
import com.demo.data.utils.DiskExecutor
import com.demo.data.utils.NetworkMonitorImpl
import com.demo.domain.util.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideDiskExecutor(): DiskExecutor {
        return DiskExecutor()
    }

    @Provides
    @AppSettingsSharedPreference
    fun provideAppSettingSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("AppSetting", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideWorkManagerInstance(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitorImpl(context)
    }

}