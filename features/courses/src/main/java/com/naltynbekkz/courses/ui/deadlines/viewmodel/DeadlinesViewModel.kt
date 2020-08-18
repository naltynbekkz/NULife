package com.naltynbekkz.courses.ui.deadlines.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.timetable.model.Occurrence
import com.naltynbekkz.core.Student
import com.naltynbekkz.timetable.repository.OccurrencesRepository
import com.naltynbekkz.core.repository.UserRepository
import com.naltynbekkz.core.Constants
import com.naltynbekkz.timetable.notifications.NotificationHandler
import com.naltynbekkz.courses.model.Deadline
import com.naltynbekkz.timetable.model.UserCourse
import com.naltynbekkz.courses.repository.DeadlineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeadlinesViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val deadlineRepository: DeadlineRepository,
    private val occurrencesRepository: OccurrencesRepository,
    private val notificationHandler: NotificationHandler,
    private val userRepository: UserRepository
) : ViewModel() {

    val userCourse: UserCourse = savedStateHandle[Constants.USER_COURSE]!!

    val deadlines = deadlineRepository.getDeadlines(userCourse)
    val tasks = occurrencesRepository.deadlines


    fun post(
        deadline: Deadline,
        allSections: Boolean,
        success: () -> Boolean,
        failure: () -> Unit
    ) {
        if (allSections) {
            deadline.section = 0
        }

        deadline.author = Student(
            userRepository.user.value!!, false
        )
        deadlineRepository.post(deadline, success, failure)
    }


    fun insert(task: Occurrence) {
        viewModelScope.launch(Dispatchers.IO) {
            val notificationId = occurrencesRepository.insert(task)

            if (task.notificationTime != null) {
                task.notificationId = notificationId
                notificationHandler.scheduleNotification(task)
            }
        }
    }

    fun delete(task: Occurrence) {
        viewModelScope.launch(Dispatchers.IO) {
            occurrencesRepository.delete(task)
            notificationHandler.cancel(task)
        }
    }
}