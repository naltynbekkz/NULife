package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import java.io.Serializable


// suggestedCourses/major/

class SuggestedCourse(
    var id: String = "",
    var name: String = ""
) : Serializable {

    constructor(ds: DataSnapshot) : this(ds.key!!, ds.value as String)

    companion object {
        fun getData(dataSnapshot: DataSnapshot): ArrayList<SuggestedCourse> {
            return ArrayList<SuggestedCourse>().apply {
                dataSnapshot.children.forEach {
                    add(SuggestedCourse(it))
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is SuggestedCourse) {
            id == other.id && name == other.name
        } else false
    }

}