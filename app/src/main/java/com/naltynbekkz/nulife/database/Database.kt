package com.naltynbekkz.nulife.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.naltynbekkz.nulife.model.Contact
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.model.Resource
import com.naltynbekkz.nulife.util.Convert

@Database(
    entities = [
        Item::class,
        Resource::class,
        Occurrence::class,
        Contact::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convert::class)
abstract class BaseDatabase : RoomDatabase() {
    abstract fun itemsDao(): ItemsDao
    abstract fun resourcesDao(): ResourcesDao
    abstract fun occurrencesDao(): OccurrencesDao
    abstract fun contactsDao(): ContactsDao
}