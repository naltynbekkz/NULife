package com.naltynbekkz.nulife.repository

import androidx.annotation.WorkerThread
import com.naltynbekkz.nulife.database.ContactsDao
import com.naltynbekkz.nulife.di.main.MainScope
import com.naltynbekkz.nulife.model.Contact
import javax.inject.Inject

@MainScope
class ContactsRepository @Inject constructor(val dao: ContactsDao) {
    val contacts by lazy {
        dao.getAll()
    }

    @WorkerThread
    suspend fun insert(contact: Contact) {
        dao.insert(contact)
    }

    @WorkerThread
    suspend fun delete(contact: Contact) {
        dao.delete(contact)
    }

}