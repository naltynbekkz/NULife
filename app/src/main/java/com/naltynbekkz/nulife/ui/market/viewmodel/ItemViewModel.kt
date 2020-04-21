package com.naltynbekkz.nulife.ui.market.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.nulife.model.Contact
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.model.Request
import com.naltynbekkz.nulife.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemViewModel(
    val item: Item,
    itemsRepository: ItemsRepository,
    private val userRatingsRepository: UserRatingsRepository,
    private val requestsRepository: RequestsRepository,
    private val savedItemsRepository: SavedItemsRepository,
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    val savedItem = savedItemsRepository.getItem(item.id)

    val contacts = contactsRepository.contacts

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

    fun insert(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactsRepository.insert(contact)
        }
    }

    private val _newRequest = MutableLiveData<Request?>()
    val newRequest: LiveData<Request?>
        get() = _newRequest

    init {
        _newRequest.postValue(null)
    }

    val itemLiveData = itemsRepository.getItem(item)

    val authorRating = userRatingsRepository.getAuthorRating(item.author.id)

    val myRating: LiveData<Long?> = userRatingsRepository.getMyRating(item)

    val request = requestsRepository.getRequest(item)


    fun editRequest() {
        _newRequest.postValue(request.value ?: Request())
    }

    fun removeRequest() {
        _newRequest.postValue(null)
    }

    fun deleteRequest() {
        _newRequest.postValue(null)
        requestsRepository.removeRequest(item)
    }

    fun request(
        contacts: ArrayList<Contact>
    ) {
        newRequest.value!!.contacts = contacts
        requestsRepository.request(item, newRequest.value!!) {
            removeRequest()
        }
    }

    fun rate(
        rating: Long,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        userRatingsRepository.rate(rating, item, success, failure)
    }

}
