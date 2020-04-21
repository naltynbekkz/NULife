package com.naltynbekkz.nulife.ui.clubs.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.repository.EventsRepository
import com.naltynbekkz.nulife.repository.OccurrencesRepository
import com.naltynbekkz.nulife.util.NotificationHandler
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    eventsRepository: EventsRepository,
    private val occurrencesRepository: OccurrencesRepository,
    private val notificationHandler: NotificationHandler
) : ViewModel() {


    private val eventId: String = savedStateHandle[com.naltynbekkz.nulife.util.Constant.EVENT_ID]!!

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<EventViewModel>

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
                notificationHandler.scheduleTaskNotification(task)
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