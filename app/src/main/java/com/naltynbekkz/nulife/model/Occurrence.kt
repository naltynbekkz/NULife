package com.naltynbekkz.nulife.model

import android.app.AlarmManager
import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.naltynbekkz.nulife.util.Constants
import com.naltynbekkz.nulife.util.Convert
import java.io.Serializable

@Entity(tableName = "occurrences")
open class Occurrence(

    var id: String = "",
    var title: String = "",
    var details: String? = null,
    var location: String? = null,
    var start: Long = 0,
    var end: Long = 0,
    var color: String? = null,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "notification_id")
    var notificationId: Long = 0,
    @ColumnInfo(name = "notification_time")
    var notificationTime: Long? = null,

    @ColumnInfo(name = "task_type")
    var taskType: String? = null,
    @ColumnInfo(name = "routine_type")
    var routineType: String? = null,

    // Task
    @ColumnInfo(name = "parent_id")
    var parentId: String? = null,
    @ColumnInfo(name = "parent_title")
    var parentTitle: String? = null,
    @ColumnInfo(name = "parent_type")
    var parentType: String? = null,

    //Routine
    var week: String? = null

) : Serializable {
    companion object {
        const val ASSOCIATE = "ASSOCIATE" // "custom" for tasks
        const val DEADLINE = "DEADLINE"
        const val EVENT = "EVENT"

        const val CUSTOM = "CUSTOM"
        const val COURSE = "COURSE"
        const val CLUB = "CLUB"

        fun get(json: String): Occurrence {
            return Gson().fromJson(json, Occurrence::class.java) as Occurrence
        }
    }

    constructor(associate: Associate?) : this(
        id = associate?.id ?: "",
        title = associate?.title ?: "",
        color = associate?.color,
        taskType = null,
        routineType = associate?.type ?: CUSTOM,
        start = 0
    )

    constructor(task: Boolean) : this(
        taskType = if (task) ASSOCIATE else null,
        routineType = if (!task) CUSTOM else null,
        start = if (task) System.currentTimeMillis() / 1000 else 0
    )

    constructor(deadline: Deadline, parent: UserCourse) : this(
        id = deadline.id,
        title = deadline.title,
        details = deadline.details,
        location = deadline.location,
        color = parent.color,
        taskType = DEADLINE,
        parentId = parent.id,
        parentTitle = parent.id,
        parentType = COURSE,
        start = deadline.timestamp
    )

    constructor(event: Event, color: String) : this(
        id = event.id,
        title = event.title,
        details = event.details,
        location = event.location,
        start = event.start,
        end = event.end ?: 0,
        color = color,
        taskType = EVENT,
        parentId = event.club.id,
        parentTitle = event.club.title,
        parentType = CLUB
    )

    fun startSeconds(): Int = if (taskType != null) Convert.getSeconds(start) else start.toInt()

    fun getDetail(): String {
        return if (location != null && details != null) {
            "$location | $details"
        } else if (location == null && details == null) {
            ""
        } else {
            "$location$details"
        }
    }

    fun getStringNotificationTime(): String? {
        return if (notificationTime != null) {

            val seconds = notificationTime!!

            return when {
                seconds <= 0 -> "a few seconds"
                seconds < 60 * 60 -> "${seconds / 60} minute(s)"
                seconds < 60 * 60 * 24 -> "${seconds / (60 * 60)} hour(s)"
                seconds < 60 * 60 * 24 * 7 -> "${seconds / (60 * 60 * 24)} day(s)"
                seconds < 60 * 60 * 24 * 30 -> "${seconds / (60 * 60 * 24 * 7)} week(s)"
                seconds < 60 * 60 * 24 * 365 -> "${seconds / (60 * 60 * 24 * 30)} month(s)"
                else -> "${seconds / (60 * 60 * 24 * 265)} year(s)"
            }

        } else null
    }

    fun getIntColor(): Int {
        return Color.parseColor(color)
    }

    fun isNew() = id.isEmpty()

    fun getTime(): String {
        return if (routineType != null) {
            Convert.secondsToTime(start) + "\n" + Convert.secondsToTime(end)
        } else {
            if (end == 0L) {
                Convert.timestampToTime(start)
            } else {
                Convert.timestampToTime(start) + " " + Convert.timestampToTime(end)
            }
        }
    }

    fun getStartDate(): String {
        return Convert.timestampToDateTime(start)
    }

    fun getEndDate(): String? {
        return if (end != 0L) {
            Convert.timestampToDateTime(end)
        } else {
            null
        }
    }

    fun getTaskDate(): String {
        return if (end != 0L && Convert.removeHours(start).timeInMillis != Convert.removeHours(end).timeInMillis) {
            Convert.timestampToDate(start) + " - " + Convert.timestampToDate(end)
        } else {
            Convert.timestampToDate(start)
        }
    }

    fun getTaskStart(): String {
        return Convert.timestampToTime(start)
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    fun getRoutineTime(): String {
        return Convert.secondsToTime(start) + " - " + Convert.secondsToTime(end)
    }

    fun getRoutineStart(): String {
        return Convert.secondsToTime(start)
    }

    fun getRoutineEnd(): String? {
        return Convert.secondsToTime(end)
    }

    fun getNotificationDetail(): String {

        val a = if (routineType != null) {
            getRoutineTime()
        } else {
            Convert.timestampToDateTime(start)
        }

        return if (location != null) "$a, $location" else a
    }

    fun getWeeks(): String? {
        week?.let {
            val string = StringBuilder()
            val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            for (i in it.indices) {
                if (string.isNotEmpty() && it[i] == '1') {
                    string.append(", ")
                }
                if (it[i] == '1') {
                    string.append(days[i])
                }
            }
            return string.toString()
        }
        return null
    }

    fun getTriggerTime(): Long {
        if (taskType != null) {
            return (start - notificationTime!!) * 1000L - System.currentTimeMillis()
        } else {
            var time =
                ((start - notificationTime!!) * 1000 + Constants.MAX_NOTIFICATION_TIME) % AlarmManager.INTERVAL_DAY - System.currentTimeMillis() + Convert.removeHours().timeInMillis
            if (time < 0) {
                time += AlarmManager.INTERVAL_DAY
            }
            return time
        }
    }

}