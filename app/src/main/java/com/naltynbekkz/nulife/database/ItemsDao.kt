package com.naltynbekkz.nulife.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.naltynbekkz.nulife.model.Item

@Dao
interface ItemsDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Query("SELECT * FROM items")
    fun getAll(): LiveData<List<Item>>

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItem(id: String): LiveData<List<Item>>

}