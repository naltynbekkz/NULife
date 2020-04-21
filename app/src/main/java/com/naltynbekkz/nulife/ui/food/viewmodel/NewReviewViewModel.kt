package com.naltynbekkz.nulife.ui.food.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.Review
import com.naltynbekkz.nulife.repository.ReviewRepository
import com.naltynbekkz.nulife.repository.UserRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class NewReviewViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val cafeId: String = savedStateHandle["cafe_id"]!!

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<NewReviewViewModel>

    fun post(
        review: Review,
        success: () -> Unit,
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