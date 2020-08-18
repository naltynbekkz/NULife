package com.naltynbekkz.nulife.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.ui.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            sendCloudNotification(
                it.title!!,
                it.body!!,
                applicationContext
            )
        }
    }

    override fun onNewToken(token: String) {
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
    }
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
        .setColor(ContextCompat.getColor(context,
            R.color.colorPrimary
        ))
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