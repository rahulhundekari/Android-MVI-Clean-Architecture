package com.demo.android_mvi_clean_architecture.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.demo.domain.repository.MovieRepository
import com.demo.domain.util.DispatcherProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext

const val SYNC_WORK_MAX_ATTEMPTS = 3

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val movieRepository: MovieRepository,
    private val dispatchers: DispatcherProvider
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(dispatchers.io) {
        return@withContext if (movieRepository.sync()) {
            Log.d("XXX", "SyncWork: doWork() called -> success")
            Result.success()
        } else {
            val lastAttempt = runAttemptCount >= SYNC_WORK_MAX_ATTEMPTS
            if (lastAttempt) {
                Log.d("XXX", "SyncWork: doWork() called -> failure")
                Result.failure()
            } else {
                Log.d("XXX", "SyncWork: doWork() called -> retry")
                Result.retry()
            }
        }
    }

    companion object {
        fun getOneTimeWorkRequest() = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()
    }
}