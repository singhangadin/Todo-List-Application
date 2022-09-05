package com.example.data.worker

import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.common.Constants
import com.example.data.R
import com.example.domain.contract.LogService
import com.example.domain.usecase.GetTaskUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters,
    val getTaskUseCase: GetTaskUseCase,
    val logService: LogService
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val taskId = params
            .inputData
            .getString(Constants.KEY_WORK_DATA)

        val taskResult = getTaskUseCase.invoke(taskId!!)
        return if (taskResult.isSuccess) {
            val task = taskResult.getOrNull()!!
            val notification = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setContentTitle(task.taskTitle)
                .setContentText(task.taskDescription)
                .setChannelId(Constants.CHANNEL_ID)
                .build()

            val channel = NotificationChannelCompat.Builder(Constants.CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW)
                .setName(context.getString(R.string.label_reminder_channel_name))
                .setDescription(context.getString(R.string.label_reminder_channel_description))
                .setShowBadge(true)
                .build()

            NotificationManagerCompat.from(context).createNotificationChannel(channel)
            NotificationManagerCompat.from(context).notify(taskId.hashCode(), notification)

            Result.success()
        } else {
            val exception = taskResult.exceptionOrNull()
            exception?.let {
                logService.logException(ReminderWorker::class.java.name, exception)
            }
            Result.failure()
        }
    }
}