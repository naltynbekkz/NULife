package com.naltynbekkz.nulife.ui.market.viewmodel

import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.repository.ItemsRepository
import com.naltynbekkz.nulife.repository.RequestsRepository
import com.naltynbekkz.nulife.repository.UserRatingsRepository

class MyItemViewModel(
    item: Item,
    private val itemsRepository: ItemsRepository,
    userRatingsRepository: UserRatingsRepository,
    requestsRepository: RequestsRepository
) : ViewModel() {

    val itemLiveData = itemsRepository.getItem(item)

    val authorRating = userRatingsRepository.getAuthorRating(item.author.id)

    val requests = requestsRepository.getRequests(item.id)

    fun delete(
        success: () -> Unit,
        failure: () -> Unit
    ) {
        itemsRepository.delete(itemLiveData.value!!, success, failure)
    }

}
