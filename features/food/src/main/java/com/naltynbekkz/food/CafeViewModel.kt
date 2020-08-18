package com.naltynbekkz.food

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.food.repository.CafesRepository

class CafeViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val cafesRepository: CafesRepository
) : ViewModel() {

    val cafeId: String = savedStateHandle["cafe_id"]!!

    val cafe = cafesRepository.getCafe(cafeId)


    fun getFeatured(mealId: String) = cafesRepository.getFeatured(cafeId, mealId)
}