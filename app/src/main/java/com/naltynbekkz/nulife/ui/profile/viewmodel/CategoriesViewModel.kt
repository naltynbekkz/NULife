package com.naltynbekkz.nulife.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.repository.NotificationsRepository
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    val notificationsRepository: NotificationsRepository
) : ViewModel() {

    val categories = notificationsRepository.data

    fun follow(id: String, exists: Boolean) {
        if (exists) {
            notificationsRepository.unFollow(id.toLowerCase())
        } else {
            notificationsRepository.follow(id.toLowerCase(), "category")
        }
    }

}