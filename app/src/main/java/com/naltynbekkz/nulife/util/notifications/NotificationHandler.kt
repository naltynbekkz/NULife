package com.naltynbekkz.nulife.util.notifications

import android.content.Context
import androidx.work.*
import com.naltynbekkz.nulife.di.main.MainScope
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.util.Constants
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@MainScope
class NotificationHandler @Inject constructor(val context: Context) {

    fun scheduleNotification(occurrence: Occurrence) {
        var time = occurrence.getTriggerTime()

        val data = Data.Builder().putString(Constants.OCCURRENCE, occurrence.toString()).build()

        val notificationWork =
            OneTimeWorkRequest
                .Builder(NotifyWorker::class.java)
                .setInitialDelay(time, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build()

        WorkManager.getInstance(context)
            .beginUniqueWork(occurrence.id, ExistingWorkPolicy.REPLACE, notificationWork)
            .enqueue()
    }

    fun cancel(occurrence: Occurrence) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(occurrence.id)
    }

}