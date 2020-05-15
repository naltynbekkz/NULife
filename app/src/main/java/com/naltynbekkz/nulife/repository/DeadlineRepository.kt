package com.naltynbekkz.nulife.repository

import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.di.main.courses.CoursesScope
import com.naltynbekkz.nulife.model.Deadline
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import javax.inject.Inject

@CoursesScope
class DeadlineRepository @Inject constructor(val database: FirebaseDatabase) {

    fun getDeadlines(userCourse: UserCourse) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("deadlines").child(userCourse.id).orderByChild("timestamp")
                .startAt((System.currentTimeMillis() / 1000).toDouble())
        )
    ) {
        Deadline.getData(userCourse, it)
    }

    fun post(
        deadline: Deadline,
        success: () -> Boolean,
        failure: () -> Unit
    ) {
        database.getReference("deadlines").child(deadline.courseId).push()
            .setValue(deadline.toHashMap())
            .addOnSuccessListener {
                success()
            }.addOnFailureListener {
                failure()
            }
    }

}