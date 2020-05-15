package com.naltynbekkz.nulife.ui.clubs.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.repository.ClubsRepository
import com.naltynbekkz.nulife.repository.NotificationsRepository
import com.naltynbekkz.nulife.repository.OccurrencesRepository
import com.naltynbekkz.nulife.repository.UserClubsRepository
import com.naltynbekkz.nulife.util.Convert
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.zhuinden.livedatacombinetuplekt.combineTuple


class ClubViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    occurrencesRepository: OccurrencesRepository,
    clubsRepository: ClubsRepository,
    private val userClubsRepository: UserClubsRepository,
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val clubId: String = savedStateHandle[com.naltynbekkz.nulife.util.Constants.CLUB_ID]!!

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<ClubViewModel>

    val club = clubsRepository.getClub(clubId)

    val events = Transformations.map(
        combineTuple(occurrencesRepository.events, clubsRepository.getClubEvents(clubId))
    ) {
        Convert.sortSavedEvents(it.first, it.second)
    }

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