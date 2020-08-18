package com.naltynbekkz.food

import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.core.repository.UserRepository
import com.naltynbekkz.food.model.Review
import com.naltynbekkz.food.repository.ReviewRepository

class NewReviewViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val cafeId: String = savedStateHandle["cafe_id"]!!

    fun post(
        review: Review,
        success: () -> Boolean,
        failure: () -> Unit,
        images: ArrayList<Uri>,
        callback: (Int) -> Unit
    ) {
        val user = userRepository.user.value!!
        review.id = user.uid
        review.name = user.name
        reviewRepository.post(cafeId, review, success, failure, images, callback)
    }

}