package com.naltynbekkz.nulife.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.repository.UserRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {

    val user = userRepository.user

}