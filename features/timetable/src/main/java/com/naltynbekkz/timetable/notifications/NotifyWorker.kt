package com.naltynbekkz.timetable.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.*
import com.naltynbekkz.core.Constants
import com.naltynbekkz.core.Convert
import com.naltynbekkz.timetable.R
import com.naltynbekkz.timetable.model.Occurrence
import java.util.concurrent.TimeUnit

class NotifyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        inputData.getString(Constants.OCCURRENCE)?.let {

            val occurrence = Occurrence.get(it)

            if (occurrence.taskType != null || occurrence.week!![Convert.getDayOfWeek(System.currentTimeMillis() / 1000 + occurrence.notificationTime!!)] == '1') {
                sendNotification(
                    occurrence,
                    applicationContext
                )
            }

            if (occurrence.routineType != null) {

                val time = occurrence.getTriggerTime()

                val notificationWork =
                    OneTimeWorkRequest
                        .Builder(NotifyWorker::class.java)
                        .setInitialDelay(time, TimeUnit.MILLISECONDS)
                        .setInputData(inputData)
                        .build()

                WorkManager.getInstance(applicationContext)
                    .beginUniqueWork(occurrence.id, ExistingWorkPolicy.REPLACE, notificationWork)
                    .enqueue()

            }

        }

        return Result.success()
    }


    fun sendNotification(
        occurrence: Occurrence,
        context: Context
    ) {
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

//        val contentIntent = Intent(context, MainActivity::class.java)
//        val contentPendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            contentIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )

        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.timetable_notification_channel_id)
        )
            .setSmallIcon(R.drawable.ic_small_notification)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setContentTitle(
                String.format(
                    context.getString(R.string.timetable_notification_title),
                    occurrence.title,
                    occurrence.getStringNotificationTime()
                )
            )
            .setContentText(occurrence.getNotificationDetail())
//            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(occurrence.notificationId.toInt(), builder.build())
    }

}

