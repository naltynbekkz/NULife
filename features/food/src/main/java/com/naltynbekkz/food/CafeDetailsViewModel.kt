package com.naltynbekkz.food

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.food.repository.CafesRepository

class CafeDetailsViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    cafesRepository: CafesRepository
) : ViewModel() {

    val cafeId: String = savedStateHandle[CAFE_ID]!!
    val cafe = cafesRepository.getCafe(cafeId)

    companion object {
        const val CAFE_ID = "cafe_id"
    }

}