package com.naltynbekkz.nulife.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.naltynbekkz.nulife.database.OccurrencesDao
import com.naltynbekkz.nulife.di.main.MainScope
import com.naltynbekkz.nulife.model.Occurrence
import javax.inject.Inject

@MainScope
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