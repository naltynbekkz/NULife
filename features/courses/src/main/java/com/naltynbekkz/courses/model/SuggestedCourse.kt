package com.naltynbekkz.courses.model

import com.google.firebase.database.DataSnapshot
import com.naltynbekkz.timetable.model.UserCourse
import java.io.Serializable


// suggestedCourses/major/

data class SuggestedCourse(
    var id: String = "",
    var name: String = ""
) : Serializable {

    constructor(ds: DataSnapshot) : this(ds.key!!, ds.value as String)

    fun toUserCourse(section: Long) =
        UserCourse(
            id = id,
            longTitle = name,
            section = section
        )

    companion object {
        fun getData(dataSnapshot: DataSnapshot): ArrayList<SuggestedCourse> {
            return ArrayList<SuggestedCourse>().apply {
                dataSnapshot.children.forEach {
                    add(SuggestedCourse(it))
                }
            }
        }
    }

}