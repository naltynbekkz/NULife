package com.naltynbekkz.food

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.food.repository.CafesRepository

class CafeMenuViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    cafesRepository: CafesRepository
) : ViewModel() {

    val cafeId: String = savedStateHandle["cafe_id"]!!

    val meals = cafesRepository.getMeals(cafeId)

}