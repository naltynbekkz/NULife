package com.naltynbekkz.timetable.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.naltynbekkz.timetable.model.Occurrence

@Dao
interface OccurrencesDao {

    @Insert
    suspend fun insert(occurrence: Occurrence): Long

    @Update
    suspend fun update(occurrence: Occurrence)

    @Query("SELECT * FROM occurrences WHERE routine_type IS NOT NULL")
    fun getRoutines(): LiveData<List<Occurrence>>

    @Query("SELECT * FROM occurrences WHERE week LIKE :week OR start BETWEEN :day AND :dayEnd")
    fun getRoutinesAndDayTasks(day: Long, dayEnd: Long, week: String): LiveData<List<Occurrence>>

    @Query("SELECT * FROM occurrences WHERE routine_type IS NOT NULL OR start BETWEEN :month AND :monthEnd")
    fun getRoutinesAndMonthTasks(month: Long, monthEnd: Long): LiveData<List<Occurrence>>

    @Query("SELECT * FROM occurrences WHERE id = :id")
    fun getById(id: String): LiveData<List<Occurrence>>

    @Delete
    suspend fun delete(vararg occurrence: Occurrence)

    @Query("SELECT * FROM occurrences WHERE task_type = :type")
    fun getByType(type: String): LiveData<List<Occurrence>>
}