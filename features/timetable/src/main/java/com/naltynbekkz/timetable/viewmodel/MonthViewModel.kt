package com.naltynbekkz.timetable.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.core.Constants
import com.naltynbekkz.core.Convert
import com.naltynbekkz.timetable.notifications.NotificationHandler
import com.naltynbekkz.timetable.repository.OccurrencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class MonthViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val occurrencesRepository: OccurrencesRepository,
    private val notificationHandler: NotificationHandler
) : ViewModel() {

    private val month: Long = savedStateHandle[Constants.MONTH]!!
    private val monthEnd = Convert.monthEnd(month)

    val occurrences =
        Transformations.map(
            occurrencesRepository.getRoutinesAndMonthTasks(month, monthEnd)
        ) { occurrences ->
            ArrayList(TreeMap<Long, ArrayList<com.naltynbekkz.timetable.model.Occurrence>>().apply {
                occurrences.forEach {
                    if (it.taskType != null) {
                        val day = Convert.removeHours(it.start).timeInMillis / 1000
                        if (!this.containsKey(day)) {
                            this[day] = ArrayList()
                        }
                        this[day]!!.add(it)
                    }
                }
                for (i in month until monthEnd step 24 * 60 * 60) {
                    occurrences.forEach {
                        if (it.routineType != null && it.week!![Convert.getDayOfWeek(i)] == '1') {
                            if (!this.containsKey(i)) {
                                this[i] = ArrayList()
                            }
                            this[i]!!.add(it)
                        }
                    }
                }
                forEach {
                    it.value.sortWith(Comparator { o1, o2 ->
                        (o1.startSeconds() - o2.startSeconds())
                    })
                }
            }.entries)
        }

    fun delete(occurrence: com.naltynbekkz.timetable.model.Occurrence) {
        viewModelScope.launch(Dispatchers.IO) {
            occurrencesRepository.delete(occurrence)
            notificationHandler.cancel(occurrence)
        }
    }
}