package com.naltynbekkz.clubs.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.clubs.repository.EventsRepository
import com.naltynbekkz.core.Constants
import com.naltynbekkz.timetable.notifications.NotificationHandler
import com.naltynbekkz.timetable.model.Occurrence
import com.naltynbekkz.timetable.repository.OccurrencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    eventsRepository: EventsRepository,
    private val occurrencesRepository: OccurrencesRepository,
    private val notificationHandler: NotificationHandler
) : ViewModel() {


    private val eventId: String = savedStateHandle[Constants.EVENT_ID]!!

    val event = eventsRepository.getEvent(eventId)
    val task =
        Transformations.map(occurrencesRepository.getById(eventId)) {
            it.firstOrNull()
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

    fun delete(occurrence: Occurrence) {
        viewModelScope.launch(Dispatchers.IO) {
//            occurrencesRepository.delete(task.value!!)
        }
//        notificationHandler.cancel(task.value!!)
    }

}