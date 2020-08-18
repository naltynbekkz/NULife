package com.naltynbekkz.courses.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.naltynbekkz.courses.model.Resource

@Dao
interface ResourcesDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(resource: Resource)

    @Update
    suspend fun update(resource: Resource)

    @Query("SELECT * FROM resources")
    fun getAll(): LiveData<List<Resource>>

    @Delete
    suspend fun delete(vararg resource: Resource)

}