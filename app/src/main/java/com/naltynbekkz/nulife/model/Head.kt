package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class Head(
    id: String,
    name: String,
    image: String?,
    var role: String
) : Student(id, name, image), Serializable {

    constructor(ds: DataSnapshot) : this(
        id = ds.key!!,
        name = ds.child("name").value as String,
        image = ds.child("image").value as String?,
        role = ds.child("role").value as String
    )

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Head) {
            id == other.id && name == other.name && image == other.image && role == other.role
        } else false
    }

    companion object {
        fun getList(ds: DataSnapshot): ArrayList<Head> {
            return ArrayList<Head>().apply {
                ds.children.forEach {
                    add(Head(it))
                }
            }
        }
    }

}