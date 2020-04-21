package com.naltynbekkz.nulife.di.main.courses

import com.naltynbekkz.nulife.database.BaseDatabase
import com.naltynbekkz.nulife.database.ItemsDao
import com.naltynbekkz.nulife.database.ResourcesDao
import dagger.Module
import dagger.Provides

@Module
object CoursesModule {

    @CoursesScope
    @JvmStatic
    @Provides
    fun provideResourcesDao(
        database: BaseDatabase
    ): ResourcesDao {
        return database.resourcesDao()
    }


}