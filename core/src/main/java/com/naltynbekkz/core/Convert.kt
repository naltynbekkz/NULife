package com.naltynbekkz.core

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.format.DateFormat
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class Convert {
    companion object {

        /**
         *  Time
         *
         *  timestamp -> time in seconds since 1970
         *  seconds -> time in seconds since last midnight
         */

        fun timestampToTimePast(secs: Long): String {
            val seconds = System.currentTimeMillis() / 1000 - secs

            return when {
                seconds <= 0 -> "0m"
                seconds < 60 * 60 -> "${seconds / 60}m"
                seconds < 60 * 60 * 24 -> "${seconds / (60 * 60)}h"
                seconds < 60 * 60 * 24 * 7 -> "${seconds / (60 * 60 * 24)}d"
                seconds < 60 * 60 * 24 * 30 -> "${seconds / (60 * 60 * 24 * 7)}w"
                seconds < 60 * 60 * 24 * 365 -> "${seconds / (60 * 60 * 24 * 30)}mo"
                else -> "${seconds / (60 * 60 * 24 * 265)}y"
            }
        }

        fun secondsToTime(secs: Long): String {
            return calendarToTime(
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, secs.toInt() / 3600)
                    set(Calendar.MINUTE, secs.toInt() / 60 % 60)
                })
        }

        fun removeHours(): Calendar {
            return removeHours(Calendar.getInstance())
        }

        fun removeHours(seconds: Long): Calendar {
            return removeHours(
                Calendar.getInstance().apply { timeInMillis = seconds * 1000 })
        }

        fun removeDays(): Calendar {
            return Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        private fun removeHours(calendar: Calendar): Calendar {
            return calendar.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

        }

        fun monthEnd(timestamp: Long): Long {
            return Calendar.getInstance().apply {
                timeInMillis = timestamp * 1000
                add(Calendar.MONTH, 1)
            }.timeInMillis / 1000
        }

        fun timestampToTime(timestamp: Long): String {
            return calendarToTime(
                Calendar.getInstance().apply {
                    timeInMillis = timestamp * 1000
                })
        }

        private fun calendarToTime(calendar: Calendar): String {
            return DateFormat.format("h:mma", calendar).toString()
        }

        fun timestampToDay(timestamp: Long): String {
            return DateFormat.format("d", Calendar.getInstance().apply {
                timeInMillis = timestamp * 1000
            }).toString()
        }

        fun timestampToWeek(timestamp: Long): String {
            return DateFormat.format("EEE", Calendar.getInstance().apply {
                timeInMillis = timestamp * 1000
            }).toString()
        }

        fun timestampToDate(timestamp: Long): String {
            return DateFormat.format("d MMMM", Calendar.getInstance().apply {
                timeInMillis = timestamp * 1000
            }).toString()
        }

        fun timestampToDateTime(timestamp: Long): String {
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp * 1000
            return DateFormat.format("h:mma, d MMM", calendar).toString()
        }

        fun getSeconds(): Int {
            return getSeconds(Calendar.getInstance())
        }

        fun getSeconds(timestamp: Long): Int {
            return getSeconds(
                Calendar.getInstance().apply { timeInMillis = timestamp * 1000 })
        }

        private fun getSeconds(date: Calendar): Int {
            return date.get(Calendar.HOUR_OF_DAY) * 60 * 60 + date.get(Calendar.MINUTE) * 60 + date.get(
                Calendar.SECOND
            )
        }

        fun getDayOfWeek(): Int {
            return getDayOfWeek(Calendar.getInstance())
        }

        private fun getDayOfWeek(date: Calendar): Int {
            val day = date.get(Calendar.DAY_OF_WEEK) - 2
            return if (day < 0) 6 else day
        }

        fun getWeekString(dayOfWeek: Int): String {
            val sb = StringBuilder()
            for (i in 0..6) {
                sb.append(if (i == dayOfWeek) "1" else "_")
            }
            return sb.toString()
        }

        fun getDayOfWeek(secs: Long): Int {
            return getDayOfWeek(
                Calendar.getInstance().apply {
                    timeInMillis = secs * 1000
                })
        }

        fun getMonth(): Int {
            return Calendar.getInstance().get(Calendar.MONTH)
        }

        fun getShortDate(timestamp: Long): String {
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp * 1000
            return DateFormat.format("dd\nMMM", calendar).toString()
        }

        fun getZoneOffset() = Calendar.getInstance().get(Calendar.ZONE_OFFSET)

        fun setSeconds(timestamp: Long, h: Int, m: Int): Long {
            return Calendar.getInstance().apply {
                timeInMillis = timestamp * 1000
                set(Calendar.HOUR_OF_DAY, h)
                set(Calendar.MINUTE, m)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis / 1000
        }

        /**
         *  Colors
         */

        fun getColor(selectedColor: Int): String {
            return String.format("#%06X", 0xFFFFFF and selectedColor)
        }


        /**
         *  Other
         */

        fun compress(uri: Uri, contentResolver: ContentResolver): ByteArray {
            @Suppress("DEPRECATION")
            var bitmap =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            contentResolver,
                            uri
                        )
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(
                        contentResolver,
                        uri
                    )
                }
            var height = 1280
            var width = 1280
            if (bitmap.height > bitmap.width) {
                width = (bitmap.width * 1280) / bitmap.height
            } else {
                height = (bitmap.height * 1280) / bitmap.width
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)


            var byteArray: ByteArray
            var currentQuality = 100
            do {
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, baos)
                byteArray = baos.toByteArray()
                currentQuality -= 5
            } while (byteArray.size >= 100 * 1024 && currentQuality >= 0)
            return byteArray
        }

        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun help(context: Context) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://t.me/nulife_contact")
            context.startActivity(intent)
        }


    }

    @TypeConverter
    fun studentToJson(value: Student): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToStudent(value: String): Student {
        return Gson().fromJson(value, Student::class.java) as Student
    }

    @TypeConverter
    fun contactsToJson(value: ArrayList<Contact>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToContacts(value: String): ArrayList<Contact> {
        val listType = object : TypeToken<ArrayList<Contact>>() {}.type
        return Gson().fromJson(value, listType) as ArrayList<Contact>
    }

    @TypeConverter
    fun listToJson(value: ArrayList<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): ArrayList<String> {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType) as ArrayList<String>
    }
}