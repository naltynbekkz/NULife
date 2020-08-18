package com.naltynbekkz.profile.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.naltynbekkz.core.repository.UserRepository

class ProfileViewModel @ViewModelInject constructor(userRepository: UserRepository) :
    ViewModel() {

    val user = userRepository.user

}