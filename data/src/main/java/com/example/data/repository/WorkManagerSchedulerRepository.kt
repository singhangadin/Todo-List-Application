package com.example.data.repository

import android.content.Context
import androidx.work.*
import com.example.common.Constants
import com.example.data.worker.ReminderWorker
import com.example.domain.contract.SchedulerRepositoryContract
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerSchedulerRepository @Inject constructor(val context: Context): SchedulerRepositoryContract {
    override fun createScheduler(taskId: String, time: Date) {
        val workManager = WorkManager.getInstance(context)
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(time.time - Date().time, TimeUnit.MILLISECONDS)
            .addTag(taskId)
            .setInputData(
                Data.Builder()
                    .putString(Constants.KEY_WORK_DATA, taskId)
                    .build()
            )
            .build()
        workManager.enqueue(workRequest)
    }

    override fun deleteScheduler(taskId: String) {
        WorkManager.getInstance(context).cancelAllWorkByTag(taskId)
    }

    override fun updateScheduler(taskId: String, time: Date) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWorkByTag(taskId)
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(time.time - Date().time, TimeUnit.MILLISECONDS)
            .addTag(taskId)
            .setInputData(
                Data.Builder()
                    .putString(Constants.KEY_WORK_DATA, taskId)
                    .build()
            )
            .build()
        workManager.enqueue(workRequest)
    }
}