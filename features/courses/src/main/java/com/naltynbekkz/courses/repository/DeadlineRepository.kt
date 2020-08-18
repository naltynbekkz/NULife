package com.naltynbekkz.courses.repository

import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.core.FirebaseQueryLiveData
import com.naltynbekkz.courses.model.Deadline
import com.naltynbekkz.timetable.model.UserCourse
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
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