package com.naltynbekkz.nulife.ui.clubs.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.repository.ClubsRepository
import com.naltynbekkz.nulife.repository.NotificationsRepository
import com.naltynbekkz.nulife.repository.OccurrencesRepository
import com.naltynbekkz.nulife.repository.UserClubsRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject


class ClubViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    occurrencesRepository: OccurrencesRepository,
    clubsRepository: ClubsRepository,
    private val userClubsRepository: UserClubsRepository,
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val clubId: String = savedStateHandle[com.naltynbekkz.nulife.util.Constant.CLUB_ID]!!

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<ClubViewModel>

    val tasks = occurrencesRepository.events

    val club = clubsRepository.getClub(clubId)

    val events = clubsRepository.getClubEvents(clubId)


    fun follow() {
        userClubsRepository.follow(club.value!!.getUserClub()) {
            notificationsRepository.follow(clubId, "club")
        }
    }

    fun unFollow() {
        userClubsRepository.unFollow(clubId) {
            notificationsRepository.unFollow(clubId)
        }
    }
}