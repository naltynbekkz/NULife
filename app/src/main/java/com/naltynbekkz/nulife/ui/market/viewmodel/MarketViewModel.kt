package com.naltynbekkz.nulife.ui.market.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.repository.ItemsRepository
import com.naltynbekkz.nulife.repository.SavedItemsRepository
import com.naltynbekkz.nulife.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MarketViewModel(
    itemsRepository: ItemsRepository,
    private val savedItemsRepository: SavedItemsRepository,
    userRepository: UserRepository
) : ViewModel() {

    val user = userRepository.user
    val items = itemsRepository.items
    val femaleItems = itemsRepository.femaleItems

    val savedItems = savedItemsRepository.items

    fun delete(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            savedItemsRepository.delete(id)
        }
    }

    fun insert(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            savedItemsRepository.insert(item)
        }
    }

}
