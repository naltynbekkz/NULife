package com.naltynbekkz.courses.repository

import android.net.Uri
import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.naltynbekkz.core.FirebaseQueryLiveData
import com.naltynbekkz.core.ImageCompressor
import com.naltynbekkz.courses.model.Question
import com.naltynbekkz.timetable.model.UserCourse
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class QuestionsRepository @Inject constructor(
    val database: FirebaseDatabase,
    val storage: FirebaseStorage,
    val imageCompressor: ImageCompressor
) {

    private val reference = database.getReference("questions")

    fun getAllQuestions(userCourse: UserCourse) = Transformations.map(
        FirebaseQueryLiveData(
            reference.child(userCourse.id).child(0.toString())
        )
    ) { Question.getData(userCourse.allSections(), it) }

    fun getSectionQuestions(userCourse: UserCourse) = Transformations.map(
        FirebaseQueryLiveData(
            reference.child(userCourse.id).child(userCourse.section.toString())
        )
    ) { Question.getData(userCourse, it) }

    fun getTopics(question: Question) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("topics").child(question.courseId)
        )
    ) { Question.dataToTopics(it) }

    fun getQuestion(question: Question) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("questions").child(question.courseId)
                .child(question.sectionId.toString()).child(question.id)
        )
    ) {
        Question(question, it)
    }

    fun post(
        question: Question,
        follow: (String) -> Unit,
        failure: () -> Unit,
        images: ArrayList<Uri>,
        done: (Int) -> Unit
    ) {
        val questionReference = database.getReference("questions").child(question.courseId)
            .child(question.sectionId.toString())
            .push()

        lateinit var upload: (Int) -> Unit
        upload = fun(i: Int) {
            if (i == images.size) {
                questionReference
                    .setValue(question.toHashMap())
                    .addOnSuccessListener {
                        follow(questionReference.key!!)
                    }.addOnFailureListener {
                        failure()
                    }
                return
            }

            val byteArray = imageCompressor.compress(images[i])

            val ref = storage.reference.child("questions")
                .child(questionReference.key!!)
                .child(i.toString())

            ref.putBytes(byteArray).continueWithTask {
                ref.downloadUrl
            }.addOnSuccessListener {
                question.images.add(it.toString())
                done(i)
                upload(i + 1)
            }.addOnFailureListener {
                failure()
            }
        }
        upload(0)
    }

    fun editQuestion(
        question: Question,
        success: () -> Boolean,
        failure: () -> Unit
    ) {
        reference
            .child(question.courseId)
            .child(question.sectionId.toString())
            .child(question.id)
            .updateChildren(
                question.toEditHashMap()
            ).addOnSuccessListener {
                success()
            }.addOnFailureListener {
                failure()
            }
    }

    fun delete(question: Question) {
        reference
            .child(question.courseId)
            .child(question.sectionId.toString())
            .child(question.id)
            .removeValue()
    }

}