package com.naltynbekkz.nulife.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.naltynbekkz.timetable.model.Occurrence
import com.naltynbekkz.courses.model.Resource
import com.naltynbekkz.core.Convert
import com.naltynbekkz.timetable.database.OccurrencesDao

@Database(
    entities = [
        Resource::class,
        Occurrence::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convert::class)
abstract class BaseDatabase : RoomDatabase() {
    abstract fun resourcesDao(): com.naltynbekkz.courses.database.ResourcesDao
    abstract fun occurrencesDao(): OccurrencesDao

    companion object {
        fun getInstance(context: Context): BaseDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                BaseDatabase::class.java,
                "database"
            ).build()
        }
    }

}