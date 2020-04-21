package com.naltynbekkz.nulife.ui.courses.courses.viewmodel

import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.repository.CoursesRepository
import com.naltynbekkz.nulife.repository.UserCoursesRepository
import javax.inject.Inject

class EnrollViewModel @Inject constructor (
    coursesRepository: CoursesRepository,
    userCoursesRepository: UserCoursesRepository
) : ViewModel() {

    val courses = coursesRepository.data

    val userCourses = userCoursesRepository.userCourses

}