package com.naltynbekkz.nulife.ui.food.viewmodel

import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.repository.CafesRepository
import javax.inject.Inject

class CafesViewModel @Inject constructor(cafesRepository: CafesRepository) : ViewModel() {

    val cafes = cafesRepository.cafes

}