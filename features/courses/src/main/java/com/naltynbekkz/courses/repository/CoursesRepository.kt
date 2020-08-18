package com.naltynbekkz.courses.repository

import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.core.FirebaseQueryLiveData
import com.naltynbekkz.core.FirebaseSingleQueryLiveData
import com.naltynbekkz.courses.model.Course
import com.naltynbekkz.courses.model.SuggestedCourse
import com.naltynbekkz.timetable.model.UserCourse
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CoursesRepository @Inject constructor(private val database: FirebaseDatabase) {

    private val reference = database.getReference("all_courses")

    fun getSuggestedCourse(courseId: String) = Transformations.map(
        FirebaseQueryLiveData(reference.child(courseId))
    ) {
        SuggestedCourse(it)
    }

    val data = Transformations.map(
        FirebaseQueryLiveData(reference)
    ) {
        SuggestedCourse.getData(it)
    }

    fun getStudentCount(userCourse: UserCourse) = Transformations.map(
        FirebaseSingleQueryLiveData(
            database.getReference("courses").child(userCourse.id)
                .child(userCourse.section.toString())
        )
    ) {
        if (it.exists()) {
            Course(userCourse.id, it).students.size
        } else {
            0
        }
    }

    fun getCourse(userCourse: UserCourse) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("courses").child(userCourse.id)
                .child(userCourse.section.toString())
        )
    ) {
        Course(userCourse.id, it)
    }

}