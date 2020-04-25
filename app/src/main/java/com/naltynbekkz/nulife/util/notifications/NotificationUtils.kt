package com.naltynbekkz.nulife.util.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.ui.MainActivity


fun sendNotification(
    occurrence: Occurrence,
    context: Context
) {
    val notificationManager = ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    ) as NotificationManager

    val contentIntent = Intent(context, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        context,
        0,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

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
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notificationManager.notify(occurrence.notificationId.toInt(), builder.build())
}

fun sendCloudNotification(
    title: String,
    details: String,
    context: Context
) {

    val contentIntent = Intent(context, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        context,
        0,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        context,
        context.getString(R.string.cloud_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_small_notification)
        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
        .setContentTitle(title)
        .setContentText(details)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    (ContextCompat.getSystemService(
        context,
        NotificationManager::class.java
    ) as NotificationManager).notify(10101, builder.build())
}