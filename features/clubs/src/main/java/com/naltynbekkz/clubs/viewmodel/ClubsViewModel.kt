package com.naltynbekkz.clubs.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.clubs.repository.ClubsRepository
import com.naltynbekkz.timetable.repository.UserClubsRepository
import com.naltynbekkz.core.Constants

class ClubsViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    clubsRepository: ClubsRepository,
    userClubsRepository: UserClubsRepository
) : ViewModel() {

    private val all: Boolean = savedStateHandle[Constants.ALL]!!
    val clubs = if (all) clubsRepository.data else userClubsRepository.userClubs

}