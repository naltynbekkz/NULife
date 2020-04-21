package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class UserClub(
    var id: String,
    var title: String,
    var logo: String
) : Serializable {

    constructor(ds: DataSnapshot) : this(
        id = ds.key!!,
        title = ds.child("title").value as String,
        logo = ds.child("logo").value as String
    )

    constructor(ds: DataSnapshot, boolean: Boolean) : this(
        id = ds.child("id").value as String,
        title = ds.child("title").value as String,
        logo = ds.child("logo").value as String
    )

    fun toHashMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("title", title)
            put("logo", logo)
        }
    }

    companion object {
        fun getData(ds: DataSnapshot): ArrayList<UserClub> {
            return ArrayList<UserClub>().apply {
                ds.children.forEach {
                    add(UserClub(it))
                }
            }
        }


        fun getHashSet(clubs: ArrayList<UserClub>?): HashSet<String> {
            return HashSet<String>().apply {
                clubs?.forEach {
                    add(it.id)
                }
            }
        }

    }

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is UserClub) {
            id == other.id && title == other.title && logo == other.logo
        } else false
    }

}
