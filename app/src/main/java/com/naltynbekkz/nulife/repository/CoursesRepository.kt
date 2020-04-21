package com.naltynbekkz.nulife.repository

import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.di.main.courses.CoursesScope
import com.naltynbekkz.nulife.model.SuggestedCourse
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import javax.inject.Inject

//@CoursesScope
class CoursesRepository @Inject constructor(database: FirebaseDatabase) {

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

}