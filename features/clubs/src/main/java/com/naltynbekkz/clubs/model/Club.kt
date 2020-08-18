package com.naltynbekkz.clubs.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.naltynbekkz.core.Student
import com.naltynbekkz.timetable.model.UserClub
import java.io.Serializable

class Club(
    var id: String,
    var title: String,
    var details: String,
    var followers: ArrayList<Student>,
    var logo: String,
    var background: String,
    var heads: ArrayList<Head>,
    var contacts: ArrayList<com.naltynbekkz.core.Contact>,
    var membership: String
) : Serializable {

    constructor(ds: DataSnapshot) : this(
        id = ds.key!!,
        title = ds.child("title").value as String,
        details = ds.child("details").value as String,
        followers = Student.getList(ds.child("followers")),
        logo = ds.child("urls").child("logo").value as String,
        background = ds.child("urls").child("background").value as String,
        heads = Head.getList(ds.child("heads")),
        membership = ds.child("membership").value as String,
        contacts = com.naltynbekkz.core.Contact.getList(ds.child("contacts"))
    )

    fun getUserClub() = UserClub(id, title, logo)

    fun following(): Boolean {
        val uid = FirebaseAuth.getInstance().uid
        for (student in followers) {
            if (student.id == uid) {
                return true
            }
        }
        return false
    }

}