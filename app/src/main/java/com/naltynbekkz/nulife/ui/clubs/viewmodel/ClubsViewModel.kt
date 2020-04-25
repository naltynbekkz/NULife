package com.naltynbekkz.nulife.ui.clubs.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.repository.ClubsRepository
import com.naltynbekkz.nulife.repository.UserClubsRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class ClubsViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    clubsRepository: ClubsRepository,
    userClubsRepository: UserClubsRepository
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<ClubsViewModel>

    private val all: Boolean = savedStateHandle[com.naltynbekkz.nulife.util.Constants.ALL]!!
    val clubs = if (all) clubsRepository.data else userClubsRepository.userClubs

}