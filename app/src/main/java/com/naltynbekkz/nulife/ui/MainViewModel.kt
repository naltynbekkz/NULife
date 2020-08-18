package com.naltynbekkz.nulife.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.repository.MainRepository

class MainViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    init {
        mainRepository.updateInstanceId()
    }

    fun signOut() = mainRepository.signOut()

    fun isSignedIn() = mainRepository.isSignedIn()

}