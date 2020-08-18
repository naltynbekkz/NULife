package com.naltynbekkz.timetable.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.naltynbekkz.core.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityScoped
class NotificationHandler @Inject constructor(@ApplicationContext val context: Context) {

    fun scheduleNotification(occurrence: com.naltynbekkz.timetable.model.Occurrence) {
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

    fun cancel(occurrence: com.naltynbekkz.timetable.model.Occurrence) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(occurrence.id)
    }

}