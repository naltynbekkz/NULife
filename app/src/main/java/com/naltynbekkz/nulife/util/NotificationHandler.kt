package com.naltynbekkz.nulife.util

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import com.naltynbekkz.nulife.di.main.MainScope
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.util.notifications.NotificationReceiver
import javax.inject.Inject

@MainScope
class NotificationHandler @Inject constructor(val context: Context) {

    fun scheduleRoutineNotification(routine: Occurrence) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val notifyPendingIntent = getNotifyPendingIntent(context, routine)

        var time =
            ((routine.start - routine.notificationTime!!) * 1000 + Constant.MAX_NOTIFICATION_TIME) % AlarmManager.INTERVAL_DAY - System.currentTimeMillis() + Convert.removeHours().timeInMillis
        if (time < 0) {
            time += AlarmManager.INTERVAL_DAY
        }

        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + time,
            AlarmManager.INTERVAL_DAY,
            notifyPendingIntent
        )
    }

    fun scheduleTaskNotification(task: Occurrence) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val triggerTime =
            SystemClock.elapsedRealtime() - System.currentTimeMillis() + (task.start - task.notificationTime!!) * 1000L

        val notifyPendingIntent = getNotifyPendingIntent(context, task)

        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            notifyPendingIntent
        )
    }

    fun cancel(occurrence: Occurrence) {

        (ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager).cancel(occurrence.notificationId.toInt())

        val notifyPendingIntent = getNotifyPendingIntent(context, occurrence)
        notifyPendingIntent.cancel()
    }

    private fun getNotifyPendingIntent(
        context: Context,
        occurrence: Occurrence
    ): PendingIntent {
        val notifyIntent = Intent(context, NotificationReceiver::class.java)
        notifyIntent.putExtra("occurrence", occurrence.toString())

        return PendingIntent.getBroadcast(
            context,
            occurrence.notificationId.toInt(),
            notifyIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: NotificationHandler? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NotificationHandler(context).also { instance = it }
            }
    }

}