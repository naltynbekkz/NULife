package com.naltynbekkz.nulife.di.main

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.naltynbekkz.nulife.database.BaseDatabase
import com.naltynbekkz.nulife.database.ContactsDao
import com.naltynbekkz.nulife.database.OccurrencesDao
import com.naltynbekkz.nulife.util.ImageCompressor
import dagger.Module
import dagger.Provides

@Module
object MainModule {

    @MainScope
    @Provides
    @JvmStatic
    fun provideDatabaseInstance(context: Context): BaseDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            BaseDatabase::class.java,
            "database"
        ).build()
    }

    @MainScope
    @Provides
    @JvmStatic
    fun provideGlideInstance(context: Context): RequestManager {
        return Glide.with(context.applicationContext)
    }

    @MainScope
    @Provides
    @JvmStatic
    fun provideFirebaseInstanceId(): FirebaseInstanceId {
        return FirebaseInstanceId.getInstance()
    }


    @MainScope
    @Provides
    @JvmStatic
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @MainScope
    @Provides
    @JvmStatic
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @MainScope
    @Provides
    @JvmStatic
    fun provideImageCompressor(context: Context): ImageCompressor {
        return ImageCompressor(context.applicationContext.contentResolver)
    }

    @MainScope
    @Provides
    @JvmStatic
    fun provideContactsDao(
        database: BaseDatabase
    ): ContactsDao {
        return database.contactsDao()
    }

    @MainScope
    @Provides
    @JvmStatic
    fun provideOccurrencesDao(
        database: BaseDatabase
    ): OccurrencesDao {
        return database.occurrencesDao()
    }

}