package com.naltynbekkz.courses.model

import com.google.firebase.database.DataSnapshot
import com.naltynbekkz.core.Convert
import com.naltynbekkz.core.Student
import com.naltynbekkz.timetable.model.Occurrence
import com.naltynbekkz.timetable.model.UserCourse
import java.io.Serializable

class Deadline(
    var courseId: String = "",
    var section: Long = 0,
    var id: String = "",
    var author: Student = Student(),
    var title: String = "",
    var timestamp: Long = System.currentTimeMillis() / 1000,
    var location: String? = null,
    var important: Boolean = false,
    var details: String? = null
) : Serializable {

    constructor(userCourse: UserCourse) : this(
        courseId = userCourse.id
    )

    constructor(userCourse: UserCourse, ds: DataSnapshot) : this(
        courseId = userCourse.id,
        section = ds.child("section").value as Long,
        id = ds.key!!,
        author = Student(ds.child("author")),
        timestamp = ds.child("timestamp").value as Long,
        title = ds.child("title").value as String,
        important = ds.child("priority").value as Long == 1L,
        location = ds.child("location").value as String?,
        details = ds.child("details").value as String?
    )

    fun toOccurrence(parent: UserCourse) =
        Occurrence(
            id = id,
            title = title,
            details = details,
            location = location,
            color = parent.color,
            taskType = Occurrence.DEADLINE,
            parentId = parent.id,
            parentTitle = parent.id,
            parentType = Occurrence.COURSE,
            start = timestamp
        )

    fun getDateTime(): String {
        return Convert.timestampToDateTime(timestamp)
    }

    fun getTime(): String {
        return Convert.timestampToTime(timestamp)
    }

    fun toHashMap(): HashMap<String, Any?> {
        return HashMap<String, Any?>().apply {
            put("section", section)
            put("author", author.toHashMap())
            put("title", title)
            put("timestamp", timestamp)
            put("location", location)
            put("priority", if (important) 1L else 0L)
            put("details", details)
        }
    }

    companion object {
        fun getData(userCourse: UserCourse, ds: DataSnapshot): ArrayList<Deadline> {
            return ArrayList<Deadline>().apply {
                ds.children.forEach {
                    add(Deadline(userCourse, it))
                }
            }
        }
    }

    fun getDetailsString(): String {
        return if (location.isNullOrEmpty()) {
            "Section-$section"
        } else {
            "Section-$section, $location"
        }
    }

    fun isValid(): Boolean {
        return title.isNotEmpty()
    }

}