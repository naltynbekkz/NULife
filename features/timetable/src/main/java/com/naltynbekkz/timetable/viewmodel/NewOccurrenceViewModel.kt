package com.naltynbekkz.timetable.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.core.Constants
import com.naltynbekkz.timetable.model.Associate
import com.naltynbekkz.timetable.model.Occurrence
import com.naltynbekkz.timetable.notifications.NotificationHandler
import com.naltynbekkz.timetable.repository.OccurrencesRepository
import com.naltynbekkz.timetable.repository.UserClubsRepository
import com.naltynbekkz.timetable.repository.UserCoursesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewOccurrenceViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    userCoursesRepository: UserCoursesRepository,
    userClubsRepository: UserClubsRepository,
    private val occurrencesRepository: OccurrencesRepository,
    private val notificationHandler: NotificationHandler
) : ViewModel() {

    val task: Occurrence by lazy {
        if (savedStateHandle.get<Occurrence>(Constants.TASK) != null) {
            savedStateHandle[Constants.TASK]!!
        } else {
            Occurrence(task = true)
        }
    }

    val routine: Occurrence by lazy {
        if (savedStateHandle.get<Occurrence>(Constants.ROUTINE) != null) {
            savedStateHandle[Constants.ROUTINE]!!
        } else {
            Occurrence(associate = savedStateHandle[Constants.ASSOCIATE])
        }
    }

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

    fun insert(occurrence: Occurrence, complete: () -> Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val notificationId = occurrencesRepository.insert(occurrence)

            if (occurrence.notificationTime != null) {
                occurrence.notificationId = notificationId
                notificationHandler.scheduleNotification(occurrence)
            }

            complete()
        }
    }


    fun update(occurrence: Occurrence, complete: () -> Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            occurrencesRepository.update(occurrence)

            notificationHandler.cancel(occurrence)

            if (occurrence.notificationTime != null) {
                notificationHandler.scheduleNotification(occurrence)
            }

            complete()
        }
    }

    fun getKey() = FirebaseDatabase.getInstance().reference.push().key!!

}