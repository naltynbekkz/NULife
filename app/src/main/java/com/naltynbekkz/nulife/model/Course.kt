package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class Course(
    var id: String? = "",
    var section: Long? = 0,
    var title: String? = "",
    var professor: String? = "Not set",
    var syllabus: String? = null,
    var students: ArrayList<Student> = ArrayList()
) : Serializable {

    constructor(id: String, dataSnapshot: DataSnapshot) : this(
        id = id,
        section = dataSnapshot.key!!.toLong(),
        title = dataSnapshot.child("long_title").value as String?,
        professor = dataSnapshot.child("professor").value as String?,
        syllabus = dataSnapshot.child("syllabus").value as String?,
        students = Student.getList(dataSnapshot.child("students"))
    )

}