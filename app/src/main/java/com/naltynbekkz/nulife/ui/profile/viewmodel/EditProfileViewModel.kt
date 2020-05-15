package com.naltynbekkz.nulife.ui.profile.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.nulife.model.Contact
import com.naltynbekkz.nulife.model.User
import com.naltynbekkz.nulife.repository.ContactsRepository
import com.naltynbekkz.nulife.repository.FemaleRepository
import com.naltynbekkz.nulife.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val contactsRepository: ContactsRepository,
    private val femaleRepository: FemaleRepository
) : ViewModel() {

    val user = userRepository.user
    val contacts = contactsRepository.contacts
    val female = femaleRepository.data

    fun insert(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactsRepository.insert(contact)
        }
    }

    fun delete(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactsRepository.delete(contact)
        }
    }

    fun editProfile(
        user: User,
        image: Uri?,
        success: () -> Boolean,
        failure: () -> Unit
    ) {
        userRepository.editProfile(user, image, success, failure)
    }

    fun apply() {
        femaleRepository.applyFemale()
    }

}