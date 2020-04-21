package com.naltynbekkz.nulife.di.main.market

import com.naltynbekkz.nulife.database.BaseDatabase
import com.naltynbekkz.nulife.database.ItemsDao
import dagger.Module
import dagger.Provides

@Module
object MarketModule {

    @MarketScope
    @Provides
    @JvmStatic
    fun provideItemsDao(
        database: BaseDatabase
    ): ItemsDao {
        return database.itemsDao()
    }


}