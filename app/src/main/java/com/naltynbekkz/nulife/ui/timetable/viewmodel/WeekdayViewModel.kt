package com.naltynbekkz.nulife.ui.timetable.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.repository.OccurrencesRepository
import com.naltynbekkz.nulife.util.Convert
import com.naltynbekkz.nulife.util.notifications.NotificationHandler
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeekdayViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    val occurrencesRepository: OccurrencesRepository,
    val notificationHandler: NotificationHandler
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<WeekdayViewModel>

    private val today: Long = savedStateHandle[com.naltynbekkz.nulife.util.Constants.TODAY]!!

    val occurrences = occurrencesRepository.getRoutinesAndDayTasks(
        today, today + 24 * 60 * 60, Convert.getWeekString(Convert.getDayOfWeek(today))
    )

    fun delete(occurrence: Occurrence) {
        viewModelScope.launch(Dispatchers.IO) {
            occurrencesRepository.delete(occurrence)
            notificationHandler.cancel(occurrence)
        }
    }
}