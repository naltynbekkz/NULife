package com.naltynbekkz.nulife.repository

import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.di.main.MainScope
import com.naltynbekkz.nulife.model.Course
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import com.naltynbekkz.nulife.util.single
import javax.inject.Inject

@MainScope
class UserCoursesRepository @Inject constructor(
    val auth: FirebaseAuth,
    val database: FirebaseDatabase
) {

    private val reference = database.getReference("users").child(auth.uid!!).child("courses")

    val userCourses = Transformations.map(
        FirebaseQueryLiveData(reference)
    ) {
        UserCourse.getData(it)
    }

    fun getProfessor(userCourse: UserCourse) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("courses").child(userCourse.id)
                .child(userCourse.section.toString()).child("professor")
        )
    ) {
        if (it.exists()) it.value as String else null
    }

    fun getCourse(userCourse: UserCourse) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("courses").child(userCourse.id)
                .child(userCourse.section.toString())
        )
    ) {
        Course(userCourse.id, it)
    }

    fun getStudentCount(userCourse: UserCourse, callback: (Int) -> Unit) {
        database.getReference("courses").child(userCourse.id).child(userCourse.section.toString())
            .single {
                if (it.exists()) {
                    callback(Course(userCourse.id, it).students.size)
                } else {
                    callback(0)
                }
            }
    }

    fun enroll(
        userCourse: UserCourse,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        reference
            .child(userCourse.id)
            .setValue(userCourse.toHashMap())
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                failure()
            }
    }

    fun edit(
        userCourse: UserCourse,
        success: () -> Unit,
        failure: () -> Unit,
        professor: String,
        color: String
    ) {
        database.getReference("users").child(auth.uid!!).child("courses").child(userCourse.id)
            .child("color")
            .setValue(color)
            .addOnSuccessListener {
                database.getReference("courses")
                    .child(userCourse.id)
                    .child(userCourse.section.toString())
                    .child("professor")
                    .setValue(professor)
                    .addOnSuccessListener {
                        success()
                    }
                    .addOnFailureListener {
                        failure()
                    }
            }
            .addOnFailureListener {
                failure()
            }
    }

    fun unEnroll(
        userCourse: UserCourse,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        reference
            .child(userCourse.id)
            .removeValue()
            .addOnSuccessListener {
                success()
            }
            .addOnFailureListener {
                failure()
            }
    }

}