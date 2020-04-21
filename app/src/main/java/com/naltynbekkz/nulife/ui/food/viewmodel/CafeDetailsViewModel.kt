package com.naltynbekkz.nulife.ui.food.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.repository.CafesRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class CafeDetailsViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    cafesRepository: CafesRepository
) : ViewModel() {

    val cafeId: String = savedStateHandle[com.naltynbekkz.nulife.util.Constant.CAFE_ID]!!


    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<CafeDetailsViewModel>

    val cafe = cafesRepository.getCafe(cafeId)

}