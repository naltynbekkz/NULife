package com.naltynbekkz.nulife.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.DataSnapshot
import java.io.Serializable

@Entity(tableName = "resources")
class Resource(
    var courseId: String? = "",
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var author: Student = Student(),
    var semester: String = "",
    var title: String = "",
    var year: Long = 0,
    var contentType: String,
    var urls: ArrayList<String> = ArrayList(),
    var professor: String? = "",
    var details: String? = "",
    @ColumnInfo(name = "content_type")
    var status: String? = PENDING // pending, loading, done
) : Serializable {

    constructor(userCourse: UserCourse) : this(userCourse, "")

    constructor(userCourse: UserCourse, contentType: String) : this(
        author = Student(),
        courseId = userCourse.id,
        contentType = contentType
    )

    constructor(userCourse: UserCourse, ds: DataSnapshot) : this(
        author = Student(ds.child("author")),
        courseId = userCourse.id,
        id = ds.key!!,
        contentType = ds.child("content_type").value as String,
        details = ds.child("details").value as String?,
        professor = ds.child("professor").value as String?,
        semester = ds.child("semester").value as String,
        title = ds.child("assessment").value as String,
        year = ds.child("year").value as Long
    ) {
        ds.child("urls").children.forEach {
            urls.add(it.value as String)
        }
    }

    fun toHashMap(): HashMap<String, Any?> {
        return HashMap<String, Any?>().apply {
            put("author", author.toHashMap())
            put("assessment", title)
            put("semester", semester)
            put("year", year)
            put("professor", professor)
            put("details", details)
            put("content_type", contentType)
            put("urls", imagesHashMap())
        }
    }

    fun getFileName(): String {
        return "$courseId-$semester-$year-$title"
    }

    fun getUriImages(): ArrayList<Uri> {
        return ArrayList<Uri>().apply {
            urls.forEach {
                add(Uri.parse(it))
            }
        }
    }

    private fun imagesHashMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            for (i in urls.indices) {
                put("$i", urls[i])
            }
        }
    }

    companion object {

        val PENDING = "pending"
        val LOADING = "loading"
        val DONE = "done"

        fun getData(
            userCourse: UserCourse,
            dataSnapshot: DataSnapshot
        ): ArrayList<Resource> {
            return ArrayList<Resource>().apply {
                dataSnapshot.children.forEach {
                    add(
                        Resource(
                            userCourse,
                            it
                        )
                    )
                }
            }
        }
    }

}