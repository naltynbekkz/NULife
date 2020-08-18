package com.naltynbekkz.food

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.food.repository.ReviewRepository

class ReviewViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    reviewRepository: ReviewRepository
) : ViewModel() {

    val cafeId: String = savedStateHandle["cafe_id"]!!

    val reviews = reviewRepository.getReviews(cafeId)

}