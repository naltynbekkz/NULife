package com.naltynbekkz.nulife.ui.food.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.repository.ReviewRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class ReviewViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    reviewRepository: ReviewRepository
) : ViewModel() {

    val cafeId: String = savedStateHandle["cafe_id"]!!

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<ReviewViewModel>

    val reviews = reviewRepository.getReviews(cafeId)

}