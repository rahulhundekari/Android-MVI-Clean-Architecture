package com.demo.android_mvi_clean_architecture

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.demo.android_mvi_clean_architecture.worker.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MviApplication : Application(), Configuration.Provider, ImageLoaderFactory {


    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()

        workManager.enqueueUniqueWork(
            SyncWorker::class.java.simpleName,
            ExistingWorkPolicy.KEEP,
            SyncWorker.getOneTimeWorkRequest()
        )
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.1)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(0.3)
                    .directory(cacheDir)
                    .build()
            }
            .build()
    }
}
