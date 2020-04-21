package com.naltynbekkz.nulife.ui.market.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.nulife.model.Contact
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.model.Student
import com.naltynbekkz.nulife.repository.ContactsRepository
import com.naltynbekkz.nulife.repository.ItemsRepository
import com.naltynbekkz.nulife.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewItemViewModel(
    private val itemsRepository: ItemsRepository,
    private val contactsRepository: ContactsRepository,
    userRepository: UserRepository
) : ViewModel() {

    val user = userRepository.user

    val contacts = contactsRepository.contacts

    fun insert(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactsRepository.insert(contact)
        }
    }

    fun post(
        item: Item,
        anonymous: Boolean,
        success: () -> Unit,
        failure: () -> Unit,
        images: ArrayList<Uri>,
        done: (Int) -> Unit
    ) {
        item.author = Student(
            user.value!!, anonymous
        )
        itemsRepository.post(item, success, failure, images, done)
    }

    fun edit(
        item: Item,
        anonymous: Boolean,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        item.author = Student(
            user.value!!, anonymous
        )
        itemsRepository.edit(item, success, failure)
    }

}