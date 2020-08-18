package com.naltynbekkz.courses.ui.courses.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.naltynbekkz.courses.repository.CoursesRepository
import com.naltynbekkz.timetable.repository.UserCoursesRepository

class EnrollViewModel @ViewModelInject constructor(
    coursesRepository: CoursesRepository,
    userCoursesRepository: UserCoursesRepository
) : ViewModel() {

    val courses = coursesRepository.data

    val userCourses = userCoursesRepository.userCourses

}