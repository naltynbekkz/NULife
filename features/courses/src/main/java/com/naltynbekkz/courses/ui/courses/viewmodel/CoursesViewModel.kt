package com.naltynbekkz.courses.ui.courses.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.naltynbekkz.courses.repository.CoursesRepository
import com.naltynbekkz.timetable.model.UserCourse
import com.naltynbekkz.timetable.repository.UserCoursesRepository

class CoursesViewModel @ViewModelInject constructor(
    private val userCoursesRepository: UserCoursesRepository,
    private val coursesRepository: CoursesRepository
) :
    ViewModel() {

    val userCourses = userCoursesRepository.userCourses

    fun unEnroll(
        userCourse: UserCourse,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        userCoursesRepository.unEnroll(userCourse, success, failure)
    }

    fun getCourse(userCourse: UserCourse) = coursesRepository.getCourse(userCourse)

}