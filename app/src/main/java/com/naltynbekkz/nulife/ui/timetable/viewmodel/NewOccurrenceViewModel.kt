package com.naltynbekkz.nulife.ui.timetable.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.nulife.model.Associate
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.repository.OccurrencesRepository
import com.naltynbekkz.nulife.repository.UserClubsRepository
import com.naltynbekkz.nulife.repository.UserCoursesRepository
import com.naltynbekkz.nulife.util.NotificationHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewOccurrenceViewModel @Inject constructor(
    userCoursesRepository: UserCoursesRepository,
    userClubsRepository: UserClubsRepository,
    val occurrencesRepository: OccurrencesRepository,
    val notificationHandler: NotificationHandler
) : ViewModel() {

    val userCourses = Transformations.map(
        userCoursesRepository.userCourses
    ) {
        Associate.getDataFromUserCourses(it)
    }

    val userClubs = Transformations.map(
        userClubsRepository.userClubs
    ) {
        Associate.getDataFromUserClubs(it)
    }

    val routines = occurrencesRepository.routines

    fun insertTask(task: Occurrence, complete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val notificationId = occurrencesRepository.insert(task)

            if (task.notificationTime != null) {
                task.notificationId = notificationId
                notificationHandler.scheduleTaskNotification(task)
            }

            complete()
        }
    }


    fun updateTask(task: Occurrence, complete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            occurrencesRepository.update(task)

            notificationHandler.cancel(task)

            if (task.notificationTime != null) {
                notificationHandler.scheduleTaskNotification(task)
            }

            complete()
        }
    }

    fun insertRoutine(routine: Occurrence, finish: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            occurrencesRepository.insert(routine)
            if (routine.notificationTime != null) {
                notificationHandler.scheduleRoutineNotification(routine)
            }
            finish()
        }
    }

    fun updateRoutine(routine: Occurrence, finish: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            occurrencesRepository.update(routine)
            notificationHandler.cancel(routine)
            if (routine.notificationTime != null) {
                notificationHandler.scheduleRoutineNotification(routine)
            }
            finish()
        }
    }

}