package com.naltynbekkz.nulife.ui.courses.courses.viewmodel

import androidx.lifecycle.*
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.repository.CoursesRepository
import com.naltynbekkz.nulife.repository.UserCoursesRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import javax.inject.Inject

class EnrollCourseViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    coursesRepository: CoursesRepository,
    private val userCoursesRepository: UserCoursesRepository
) : ViewModel() {

    private val courseId: String = savedStateHandle["course_id"]!!

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<EnrollCourseViewModel>

    val userCourse = Transformations.map(
        coursesRepository.getSuggestedCourse(courseId)
    ) {
        UserCourse(it)
    }

    private val _students = MutableLiveData<Int?>()
    val students: LiveData<Int?>
        get() = _students

    fun enroll(userCourse: UserCourse, success: () -> Unit, failure: () -> Unit) {
        userCoursesRepository.enroll(userCourse, success, failure)
    }

    fun setStudentCount() {
        _students.postValue(null)
        userCoursesRepository.getStudentCount(userCourse.value!!) {
            _students.postValue(it)
        }
    }

}