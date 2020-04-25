package com.naltynbekkz.nulife.ui.timetable.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.Associate
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.repository.OccurrencesRepository
import com.naltynbekkz.nulife.repository.UserClubsRepository
import com.naltynbekkz.nulife.repository.UserCoursesRepository
import com.naltynbekkz.nulife.util.Constants
import com.naltynbekkz.nulife.util.notifications.NotificationHandler
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewOccurrenceViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    userCoursesRepository: UserCoursesRepository,
    userClubsRepository: UserClubsRepository,
    val occurrencesRepository: OccurrencesRepository,
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

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<NewOccurrenceViewModel>

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

    fun insert(occurrence: Occurrence, complete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val notificationId = occurrencesRepository.insert(occurrence)

            if (occurrence.notificationTime != null) {
                occurrence.notificationId = notificationId
                notificationHandler.scheduleNotification(occurrence)
            }

            complete()
        }
    }


    fun update(occurrence: Occurrence, complete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            occurrencesRepository.update(occurrence)

            notificationHandler.cancel(occurrence)

            if (occurrence.notificationTime != null) {
                notificationHandler.scheduleNotification(occurrence)
            }

            complete()
        }
    }

}