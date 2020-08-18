package com.naltynbekkz.clubs.model

import com.google.firebase.database.DataSnapshot
import com.naltynbekkz.core.Convert
import com.naltynbekkz.timetable.model.Occurrence
import com.naltynbekkz.timetable.model.UserClub
import java.io.Serializable

data class Event(
    var club: UserClub,
    var id: String,
    var title: String,
    var start: Long,
    var end: Long?,
    var details: String,
    var images: ArrayList<String> = ArrayList(),
    var location: String?,
    var registration: Registration?,
    var saved: Boolean = false
) : Serializable {

    constructor(ds: DataSnapshot) : this(
        club = UserClub(ds.child("club"), true),
        id = ds.key!!,
        title = ds.child("title").value as String,
        start = ds.child("start").value as Long,
        end = ds.child("end").value as Long?,
        details = ds.child("details").value as String,
        location = ds.child("location").value as String?,
        registration = null
    ) {
        ds.child("urls").children.forEach {
            images.add(it.value as String)
        }
        if (ds.child("registration").exists()) {
            registration = Registration(ds.child("registration"))
        }
    }

    fun getTime(): String {
        return if (end != null) {
            "${Convert.timestampToTime(start)} - ${Convert.timestampToTime(end!!)}"
        } else {
            Convert.timestampToTime(start)
        }
    }

    fun getDate(): String {
        return if (end != null) {
            if (Convert.removeHours(start).timeInMillis == Convert.removeHours(end!!).timeInMillis) {
                Convert.timestampToDate(start)
            } else {
                "${Convert.timestampToDate(start)} - ${Convert.timestampToDate(end!!)}"
            }
        } else {
            Convert.timestampToDate(start)
        }
    }

    fun toOccurrence(color: String) =
        Occurrence(
            id = id,
            title = title,
            details = details,
            location = location,
            start = start,
            end = end ?: 0,
            color = color,
            taskType = Occurrence.EVENT,
            parentId = club.id,
            parentTitle = club.title,
            parentType = Occurrence.CLUB
        )

    fun getShare(): String {
        return StringBuilder().apply {
            append(title)
            append("\n\nTime: ${getTime()}")
            append("\nDate: ${getDate()}")
            append("\nLocation: $location")
            registration?.let {
                append("\nRegistration: ${it.link}")
            }
            append("\n\n$details")
        }.toString()
    }

    fun getShortDate() = Convert.getShortDate(start)

    companion object {
        fun getData(ds: DataSnapshot): ArrayList<Event> {
            return ArrayList<Event>().apply {
                ds.children.forEach {
                    add(Event(it))
                }
            }
        }

    }

}