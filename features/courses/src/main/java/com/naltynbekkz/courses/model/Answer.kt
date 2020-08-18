package com.naltynbekkz.courses.model

import com.google.firebase.database.DataSnapshot
import com.naltynbekkz.core.Convert
import com.naltynbekkz.core.Student
import java.io.Serializable

class Answer(
    var courseId: String = "",
    var sectionId: Long = 0L,
    var questionId: String = "",
    var id: String = "",
    var author: Student = Student(),
    var timestamp: Long = System.currentTimeMillis() / 1000,
    var body: String = "",
    var images: ArrayList<String> = ArrayList(),
    var comments: ArrayList<Comment> = ArrayList(),
    var votes: ArrayList<Vote> = ArrayList()
) : Serializable {

    constructor(question: Question) : this(
        courseId = question.courseId,
        sectionId = question.sectionId,
        questionId = question.id
    )

    constructor(question: Question, ds: DataSnapshot) : this(
        courseId = question.courseId,
        sectionId = question.sectionId,
        questionId = question.id,
        id = ds.key!!,
        author = Student(ds.child("author")),
        timestamp = ds.child("timestamp").value as Long,
        body = ds.child("body").value as String,
        comments = Comment.dataToComments(ds.child("comments")),
        votes = Vote.dataToVotes(ds.child("votes"))
    ) {
        ds.child("urls").children.forEach {
            images.add(it.value as String)
        }
    }

    fun toHashMap(): HashMap<String, Any?> {
        return HashMap<String, Any?>().apply {
            put("author", author.toHashMap())
            put("timestamp", timestamp)
            put("body", body)
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

    val question
        get() = Question(
            courseId = courseId,
            sectionId = sectionId,
            id = questionId
        )

    companion object {
        fun getData(question: Question, ds: DataSnapshot): ArrayList<Answer> {
            return ArrayList<Answer>().apply {
                ds.children.forEach {
                    add(Answer(question, it))
                }
            }
        }
    }

    fun getTime(): String = Convert.timestampToTimePast(timestamp)

    fun upVotes(): Int {
        var a = 0
        votes.forEach {
            if (it.vote)
                a++
        }
        return a
    }

    fun downVotes(): Int {
        var a = 0
        votes.forEach {
            if (!it.vote)
                a++
        }
        return a
    }

    fun getVote(id: String): Boolean? {
        votes.forEach {
            if (it.uid == id) {
                return it.vote
            }
        }
        return null
    }

    fun isValid(): Boolean {
        return body.isNotEmpty()
    }

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Answer) {
            courseId == other.courseId && questionId == other.questionId && sectionId == other.sectionId && id == other.id && author == other.author && timestamp == other.timestamp && body == other.body && images == other.images && comments == other.comments && votes == other.votes
        } else false
    }

}