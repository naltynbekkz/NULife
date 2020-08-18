package com.naltynbekkz.courses.repository

import android.net.Uri
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.naltynbekkz.core.FirebaseQueryLiveData
import com.naltynbekkz.core.ImageCompressor
import com.naltynbekkz.courses.model.Answer
import com.naltynbekkz.courses.model.Comment
import com.naltynbekkz.courses.model.Question
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AnswersRepository @Inject constructor(
    val auth: FirebaseAuth,
    val database: FirebaseDatabase,
    val storage: FirebaseStorage,
    val imageCompressor: ImageCompressor
) {

    fun getAnswers(question: Question) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("answers").child(question.courseId)
                .child(question.sectionId.toString()).child(question.id)
        )
    ) {
        Answer.getData(question, it)
    }

    fun getAnswer(answer: Answer) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("answers").child(answer.courseId)
                .child(answer.sectionId.toString()).child(answer.questionId).child(answer.id)
        )
    ) {
        Answer(answer.question, it)
    }

    fun post(
        answer: Answer,
        failure: () -> Unit,
        images: ArrayList<Uri>,
        success: () -> Boolean,
        done: (Int) -> Unit
    ) {
        val answerReference = database.getReference("answers").child(answer.courseId)
            .child(answer.sectionId.toString()).child(answer.questionId)
            .push()

        lateinit var upload: (Int) -> Unit

        upload = fun(i: Int) {
            if (i == images.size) {
                answerReference.setValue(
                    answer.toHashMap()
                ).addOnSuccessListener {
                    success()
                }.addOnFailureListener {
                    failure()
                }
                return
            }

            val byteArray = imageCompressor.compress(images[i])

            val ref = storage.reference.child("answers")
                .child(answerReference.key!!)
                .child(i.toString())

            ref.putBytes(byteArray).continueWithTask {
                ref.downloadUrl
            }.addOnSuccessListener {
                answer.images.add(it.toString())
                done(i)
                upload(i + 1)
            }.addOnFailureListener {
                failure()
            }
        }

        upload(0)
    }

    fun editAnswer(
        answer: Answer,
        success: () -> Boolean,
        failure: () -> Unit
    ) {
        database.getReference("answers")
            .child(answer.courseId)
            .child(answer.sectionId.toString())
            .child(answer.questionId)
            .child(answer.id)
            .updateChildren(
                answer.toHashMap()
            ).addOnSuccessListener {
                success()
            }.addOnFailureListener {
                failure()
            }
    }

    fun delete(answer: Answer) {
        database.getReference("answers").child(answer.courseId)
            .child(answer.sectionId.toString()).child(answer.questionId)
            .child(answer.id).removeValue()
    }

    fun vote(answer: Answer, vote: Boolean?) {
        database.getReference("answers").child(answer.courseId)
            .child(answer.sectionId.toString()).child(answer.questionId).child(answer.id)
            .child("votes").child(auth.uid!!).child("vote")
            .setValue(vote)
    }

    fun comment(
        answer: Answer,
        comment: Comment,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        if (comment.id.isEmpty()) {
            database.getReference("answers")
                .child(answer.courseId)
                .child(answer.sectionId.toString())
                .child(answer.questionId)
                .child(answer.id)
                .child("comments")
                .push()
                .setValue(comment.toHashMap())
        } else {
            database.getReference("answers").child(answer.courseId)
                .child(answer.sectionId.toString()).child(answer.questionId).child(answer.id)
                .child("comments")
                .child(comment.id)
                .updateChildren(comment.toHashMap())
        }.addOnFailureListener {
            failure()
        }.addOnSuccessListener {
            success()
        }
    }

    fun deleteComment(answer: Answer, comment: Comment) {
        database.getReference("answers")
            .child(answer.courseId)
            .child(answer.sectionId.toString())
            .child(answer.questionId)
            .child(answer.id)
            .child("comments")
            .child(comment.id)
            .removeValue()
    }

}