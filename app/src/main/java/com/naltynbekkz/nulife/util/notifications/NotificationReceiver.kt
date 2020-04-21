package com.naltynbekkz.nulife.util.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.util.Convert

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager

        if (intent.extras != null) {

            val occurrence = Occurrence.get(intent.extras?.getString("occurrence")!!)

            if (occurrence.taskType != null || occurrence.week!![Convert.getDayOfWeek(System.currentTimeMillis() / 1000 + occurrence.notificationTime!!)] == '1') {
                notificationManager.sendNotification(
                    occurrence,
                    context
                )
            }
        }

    }

}