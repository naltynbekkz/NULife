package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import com.naltynbekkz.nulife.util.Convert
import java.io.Serializable

class Comment(
    var id: String = "",
    var author: Student = Student(),
    var timestamp: Long = System.currentTimeMillis() / 1000,
    var body: String = ""
) : Serializable {

    constructor(ds: DataSnapshot) : this(
        id = ds.key!!,
        author = Student(ds.child("author")),
        timestamp = ds.child("timestamp").value as Long,
        body = ds.child("body").value as String
    )

    fun toHashMap(): HashMap<String, Any?> {
        return HashMap<String, Any?>().apply {
            put("author", author.toHashMap())
            put("timestamp", timestamp)
            put("body", body)
        }
    }

    companion object {
        fun dataToComments(ds: DataSnapshot): ArrayList<Comment> {
            return ArrayList<Comment>().apply {
                ds.children.forEach {
                    add(Comment(it))
                }
            }
        }
    }

    fun isOld(): Boolean {
        return id.isNotEmpty()
    }

    fun getTimePast(): String {
        return Convert.timestampToTimePast(timestamp)
    }


    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Comment) {
            id == other.id && author == other.author && timestamp == other.timestamp && body == other.body
        } else false
    }

}