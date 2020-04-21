package com.naltynbekkz.nulife.ui.courses.courses.viewmodel

import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.repository.UserCoursesRepository
import javax.inject.Inject

class CoursesViewModel @Inject constructor (val userCoursesRepository: UserCoursesRepository) : ViewModel() {

    val userCourses = userCoursesRepository.userCourses

    fun unEnroll(
        userCourse: UserCourse,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        userCoursesRepository.unEnroll(userCourse, success, failure)
    }

    fun getCourse(userCourse: UserCourse) = userCoursesRepository.getCourse(userCourse)

}