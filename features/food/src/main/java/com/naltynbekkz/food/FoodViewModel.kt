package com.naltynbekkz.food

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.naltynbekkz.food.repository.CafesRepository


class FoodViewModel @ViewModelInject constructor(cafesRepository: CafesRepository) : ViewModel() {

    val cafes = cafesRepository.cafes

}