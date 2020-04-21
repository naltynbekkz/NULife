package com.naltynbekkz.nulife.ui

import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.repository.MainRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    mainRepository: MainRepository
) : ViewModel() {

    val auth = mainRepository.auth

    init {
        mainRepository.updateInstanceId()
    }
}