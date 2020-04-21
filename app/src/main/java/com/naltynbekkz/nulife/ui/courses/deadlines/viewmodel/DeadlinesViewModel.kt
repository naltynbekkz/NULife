package com.naltynbekkz.nulife.ui.courses.deadlines.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.Deadline
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.model.Student
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.repository.DeadlineRepository
import com.naltynbekkz.nulife.repository.OccurrencesRepository
import com.naltynbekkz.nulife.repository.UserRepository
import com.naltynbekkz.nulife.util.Constant
import com.naltynbekkz.nulife.util.NotificationHandler
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeadlinesViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val deadlineRepository: DeadlineRepository,
    private val occurrencesRepository: OccurrencesRepository,
    private val notificationHandler: NotificationHandler,
    private val userRepository: UserRepository
) : ViewModel() {

    val userCourse: UserCourse = savedStateHandle[Constant.USER_COURSE]!!

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<DeadlinesViewModel>

    val deadlines = deadlineRepository.getDeadlines(userCourse)
    val tasks = occurrencesRepository.deadlines


    fun post(
        deadline: Deadline,
        allSections: Boolean,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        if (allSections){
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
                notificationHandler.scheduleTaskNotification(task)
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