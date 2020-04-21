package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import com.naltynbekkz.nulife.util.Convert
import java.io.Serializable

class Question(
    var courseId: String = "",
    var sectionId: Long = 0,
    var id: String = "",
    var author: Student = Student(),
    var answerCount: Long = 0,
    var resolved: Boolean = false,
    var timestamp: Long = System.currentTimeMillis() / 1000,
    var title: String = "",
    var details: String = "",
    var topic: String = "",
    var images: ArrayList<String> = ArrayList(),
    var following: Boolean = false
) : Serializable {

    constructor(userCourse: UserCourse) : this(
        courseId = userCourse.id,
        sectionId = userCourse.section
    )

    constructor(userCourse: UserCourse, ds: DataSnapshot) : this(
        courseId = userCourse.id,
        sectionId = userCourse.section,
        id = ds.key!!,
        author = Student(ds.child("author")),
        answerCount = ds.child("answer_count").value as Long,
        resolved = ds.child("resolved").value as Boolean,
        timestamp = ds.child("timestamp").value as Long,
        title = ds.child("title").value as String,
        topic = ds.child("topic").value as String,
        details = ds.child("details").value as String
    ) {
        ds.child("urls").children.forEach {
            images.add(it.value as String)
        }
    }

    constructor(question: Question, ds: DataSnapshot) : this(
        courseId = question.courseId,
        sectionId = question.sectionId,
        id = ds.key!!,
        author = Student(ds.child("author")),
        answerCount = ds.child("answer_count").value as Long,
        resolved = ds.child("resolved").value as Boolean,
        timestamp = ds.child("timestamp").value as Long,
        title = ds.child("title").value as String,
        topic = ds.child("topic").value as String,
        details = ds.child("details").value as String
    ) {
        ds.child("urls").children.forEach {
            images.add(it.value as String)
        }
    }

    fun toEditHashMap(): HashMap<String, Any> {
        return HashMap<String, Any>().apply {
            put("title", title)
            put("details", details)
            put("author", author.toHashMap())
        }
    }

    fun toHashMap(): HashMap<String, Any> {
        return HashMap<String, Any>().apply {
            put("author", author.toHashMap())
            put("answer_count", answerCount)
            put("resolved", resolved)
            put("timestamp", timestamp)
            put("title", title)
            put("details", details)
            put("topic", topic)
            put("urls", imagesHashMap())
        }
    }

    private fun imagesHashMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            for (i in images.indices) {
                put("$i", images[i])
            }
        }
    }

    fun getTimePast(): String {
        return Convert.timestampToTimePast(timestamp)
    }

    companion object {
        fun getData(userCourse: UserCourse, ds: DataSnapshot): ArrayList<Question> {
            return ArrayList<Question>().apply {
                ds.children.forEach {
                    add(Question(userCourse, it))
                }
            }
        }


        fun dataToTopics(ds: DataSnapshot): ArrayList<String> {
            return ArrayList<String>().apply {
                ds.children.forEach {
                    add(it.value as String)
                }
            }
        }

    }

    fun isValid(): Boolean {
        return title.isNotEmpty() && details.isNotEmpty() && topic.isNotEmpty()
    }

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Question) {
            id == other.id && title == other.title && courseId == other.courseId && sectionId == other.sectionId && author == other.author && details == other.details && answerCount == other.answerCount && resolved == other.resolved && timestamp == other.timestamp && topic == other.topic && images == other.images && following == other.following

        } else false
    }


}