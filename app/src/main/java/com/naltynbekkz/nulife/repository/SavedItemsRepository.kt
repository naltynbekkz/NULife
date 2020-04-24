package com.naltynbekkz.nulife.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.naltynbekkz.nulife.database.ItemsDao
import com.naltynbekkz.nulife.di.main.market.MarketScope
import com.naltynbekkz.nulife.model.Item
import javax.inject.Inject

@MarketScope
class SavedItemsRepository @Inject constructor(val itemsDao: ItemsDao) {
    val items: LiveData<List<Item>> = itemsDao.getAll()

    @WorkerThread
    suspend fun update(item: Item) {
        itemsDao.update(item)
    }

    @WorkerThread
    suspend fun insert(item: Item) {
        itemsDao.insert(item)
    }

    @WorkerThread
    suspend fun delete(id: String) {
        itemsDao.delete(id)
    }

    @WorkerThread
    fun getItem(id: String) =
        itemsDao.getItem(id)

}