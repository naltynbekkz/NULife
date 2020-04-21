package com.naltynbekkz.nulife.ui.courses.courses.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.repository.UserCoursesRepository
import com.naltynbekkz.nulife.util.Constant
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import javax.inject.Inject

class EditCourseViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val userCoursesRepository: UserCoursesRepository
) : ViewModel() {

    val userCourse: UserCourse = savedStateHandle[Constant.USER_COURSE]!!

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<EditCourseViewModel>

    val professor = userCoursesRepository.getProfessor(userCourse)

    fun edit(success: () -> Unit, failure: () -> Unit, professor: String, color: String) {
        userCoursesRepository.edit(userCourse, success, failure, professor, color)
    }
}