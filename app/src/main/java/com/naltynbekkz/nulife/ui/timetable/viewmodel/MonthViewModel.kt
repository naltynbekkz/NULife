package com.naltynbekkz.nulife.ui.timetable.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.repository.OccurrencesRepository
import com.naltynbekkz.nulife.util.Convert
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class MonthViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    val occurrencesRepository: OccurrencesRepository
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory: ViewModelAssistedFactory<MonthViewModel>

    private val month: Long = savedStateHandle[com.naltynbekkz.nulife.util.Constant.MONTH]!!
    private val monthEnd = Convert.monthEnd(month)

    val occurrences =
        Transformations.map(
            occurrencesRepository.getRoutinesAndMonthTasks(month, monthEnd)
        ) { occurrences ->
            ArrayList(TreeMap<Long, ArrayList<Occurrence>>().apply {
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

    fun delete(occurrence: Occurrence) {
        viewModelScope.launch(Dispatchers.IO) {
            occurrencesRepository.delete(occurrence)
        }
    }
}