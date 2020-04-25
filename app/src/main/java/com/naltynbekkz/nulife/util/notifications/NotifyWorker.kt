package com.naltynbekkz.nulife.util.notifications

import android.content.Context
import androidx.work.*
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.util.Constants
import com.naltynbekkz.nulife.util.Convert
import java.util.concurrent.TimeUnit

class NotifyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        inputData.getString(Constants.OCCURRENCE)?.let {

            val occurrence = Occurrence.get(it)

            if (occurrence.taskType != null || occurrence.week!![Convert.getDayOfWeek(System.currentTimeMillis() / 1000 + occurrence.notificationTime!!)] == '1') {
                sendNotification(occurrence, applicationContext)
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


}