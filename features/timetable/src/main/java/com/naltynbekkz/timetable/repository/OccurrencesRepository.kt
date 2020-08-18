package com.naltynbekkz.timetable.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.naltynbekkz.timetable.database.OccurrencesDao
import com.naltynbekkz.timetable.model.Occurrence
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class OccurrencesRepository @Inject constructor(private val dao: OccurrencesDao) {

    fun getRoutinesAndDayTasks(day: Long, dayEnd: Long, week: String) =
        dao.getRoutinesAndDayTasks(day, dayEnd, week)

    fun getRoutinesAndMonthTasks(month: Long, monthEnd: Long) =
        dao.getRoutinesAndMonthTasks(month, monthEnd)

    val deadlines by lazy {
        dao.getByType(Occurrence.DEADLINE)
    }

    val events by lazy {
        dao.getByType(Occurrence.EVENT)
    }

    val routines by lazy {
        dao.getRoutines()
    }

    @WorkerThread
    suspend fun insert(occurrence: Occurrence): Long {
        return dao.insert(occurrence)
    }

    @WorkerThread
    suspend fun update(occurrence: Occurrence) {
        dao.update(occurrence)
    }

    @WorkerThread
    suspend fun delete(occurrence: Occurrence) {
        dao.delete(occurrence)
    }

    @WorkerThread
    fun getById(id: String): LiveData<List<Occurrence>> {
        return dao.getById(id)
    }
}