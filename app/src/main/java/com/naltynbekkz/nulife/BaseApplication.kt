package com.naltynbekkz.nulife

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = sharedPreferences.getString("theme", "default")
        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                "light" -> AppCompatDelegate.MODE_NIGHT_NO
                "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                "battery" -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        createChannel(
            getString(R.string.timetable_notification_channel_id),
            getString(R.string.timetable_notification_channel_name),
            getString(R.string.timetable_notification_channel_description)
        )

        createChannel(
            getString(R.string.cloud_notification_channel_id),
            getString(R.string.cloud_notification_channel_name),
            getString(R.string.cloud_notification_channel_description)
        )

    }

    private fun createChannel(channelId: String, channelName: String, channelDescription: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
                enableLights(false)
                enableVibration(false)
                lightColor = Color.RED
                description = channelDescription
            }

            getSystemService(NotificationManager::class.java)!!
                .createNotificationChannel(notificationChannel)

        }
    }
}