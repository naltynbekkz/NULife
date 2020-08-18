package com.naltynbekkz.courses.ui.courses.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.naltynbekkz.courses.repository.CoursesRepository
import com.naltynbekkz.timetable.model.UserCourse
import com.naltynbekkz.timetable.repository.UserCoursesRepository
import com.zhuinden.livedatacombinetuplekt.combineTuple

class EnrollCourseViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val coursesRepository: CoursesRepository,
    private val userCoursesRepository: UserCoursesRepository
) : ViewModel() {

    private val courseId: String = savedStateHandle["course_id"]!!

    private val _section = MutableLiveData(1L)
    val section: LiveData<Long> get() = _section

    fun changeSection(section: Long) {
        _section.value = section
    }

    val userCourse = Transformations.map(
        combineTuple(coursesRepository.getSuggestedCourse(courseId), section)
    ) {
        it.first?.toUserCourse(it.second ?: DEFAULT_SECTION)
    }

    val students = Transformations.switchMap(
        userCourse
    ) {
        it?.let { coursesRepository.getStudentCount(it) }
    }

    fun enroll(userCourse: UserCourse, success: () -> Unit, failure: () -> Unit) {
        userCoursesRepository.enroll(userCourse, success, failure)
    }

    companion object {
        const val DEFAULT_SECTION = 1L
    }

}