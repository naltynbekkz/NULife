package com.naltynbekkz.courses.ui.courses.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.core.Constants
import com.naltynbekkz.timetable.model.UserCourse
import com.naltynbekkz.timetable.repository.UserCoursesRepository

class EditCourseViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val userCoursesRepository: UserCoursesRepository
) : ViewModel() {

    val userCourse: UserCourse = savedStateHandle[Constants.USER_COURSE]!!

    val professor = userCoursesRepository.getProfessor(userCourse)

    fun edit(success: () -> Unit, failure: () -> Unit, professor: String, color: String) {
        userCoursesRepository.edit(userCourse, success, failure, professor, color)
    }
}