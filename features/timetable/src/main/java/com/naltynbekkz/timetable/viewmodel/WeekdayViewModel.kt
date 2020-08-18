package com.naltynbekkz.timetable.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.core.Constants
import com.naltynbekkz.core.Convert
import com.naltynbekkz.timetable.notifications.NotificationHandler
import com.naltynbekkz.timetable.repository.OccurrencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeekdayViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    val occurrencesRepository: OccurrencesRepository,
    val notificationHandler: NotificationHandler
) : ViewModel() {

    private val today: Long = savedStateHandle[Constants.TODAY]!!

    val occurrences = occurrencesRepository.getRoutinesAndDayTasks(
        today, today + 24 * 60 * 60, Convert.getWeekString(
            Convert.getDayOfWeek(today)
        )
    )

    fun delete(occurrence: com.naltynbekkz.timetable.model.Occurrence) {
        viewModelScope.launch(Dispatchers.IO) {
            occurrencesRepository.delete(occurrence)
            notificationHandler.cancel(occurrence)
        }
    }
}