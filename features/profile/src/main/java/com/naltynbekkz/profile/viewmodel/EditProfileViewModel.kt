package com.naltynbekkz.profile.viewmodel

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.naltynbekkz.core.User
import com.naltynbekkz.core.repository.UserRepository

class EditProfileViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val user = userRepository.user

    fun editProfile(
        user: User,
        image: Uri?,
        success: () -> Boolean,
        failure: () -> Unit
    ) {
        userRepository.editProfile(user, image, success, failure)
    }
}