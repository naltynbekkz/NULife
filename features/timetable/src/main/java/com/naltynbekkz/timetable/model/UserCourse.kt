package com.naltynbekkz.timetable.model

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

data class UserCourse(
    var id: String = "",
    var section: Long = 1,
    var color: String = "#FFFFFF",
    var longTitle: String = ""
) : Serializable {

    constructor(ds: DataSnapshot) : this(
        id = ds.key!!,
        color = ds.child("color").value as String,
        longTitle = ds.child("long_title").value as String
    ) {
        section = try {
            ds.child("section").value as Long
        } catch (e: Exception) {
            (ds.child("section").value as String).toLong()
        }
    }

    fun toHashMap(): HashMap<String, Any> {
        return HashMap<String, Any>().apply {
            put("section", section)
            put("color", color)
            put("long_title", longTitle)
        }
    }

    fun allSections(): UserCourse {
        return UserCourse(
            id,
            0,
            color,
            longTitle
        )
    }

    companion object {
        fun getData(dataSnapshot: DataSnapshot): ArrayList<UserCourse> {
            return ArrayList<UserCourse>().apply {
                dataSnapshot.children.forEach {
                    add(UserCourse(it))
                }
            }
        }
    }

}