package com.naltynbekkz.nulife.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.naltynbekkz.nulife.model.Contact

@Dao
interface ContactsDao {

    @Insert
    suspend fun insert(contact: Contact)

    @Query("SELECT * FROM contacts")
    fun getAll(): LiveData<List<Contact>>

    @Delete
    suspend fun delete(vararg contacts: Contact)

}