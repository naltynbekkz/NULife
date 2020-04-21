package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

open class Student(
    var id: String,
    var name: String,
    var image: String?
) : Serializable {

    constructor() : this("", "", "")

    constructor(ds: DataSnapshot) : this(
        id = ds.child("uid").value as String,
        name = ds.child("name").value as String,
        image = ds.child("image").value as String?
    )

    constructor(user: User, anonymous: Boolean) : this(
        id = if (anonymous) user.anonymousId else user.uid,
        name = if (anonymous) "Anonymous" else user.name,
        image = if (anonymous) null else user.image
    )

    fun toHashMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("uid", id)
            put("name", name)
            image?.let { put("image", it) }
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Student) {
            id == other.id && name == other.name && image == other.image
        } else false
    }

    companion object {
        fun getList(ds: DataSnapshot): ArrayList<Student> {
            return ArrayList<Student>().apply {
                ds.children.forEach {
                    add(
                        Student(
                            id = it.key!!,
                            name = it.child("name").value as String,
                            image = it.child("image").value as String
                        )
                    )
                }
            }
        }
    }

}