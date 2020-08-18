package com.naltynbekkz.clubs.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.naltynbekkz.clubs.Sorting
import com.naltynbekkz.clubs.repository.ClubsRepository
import com.naltynbekkz.timetable.repository.UserClubsRepository
import com.naltynbekkz.core.Constants.Companion.CLUB_ID
import com.naltynbekkz.core.repository.NotificationsRepository
import com.naltynbekkz.timetable.repository.OccurrencesRepository
import com.zhuinden.livedatacombinetuplekt.combineTuple


class ClubViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    occurrencesRepository: OccurrencesRepository,
    clubsRepository: ClubsRepository,
    private val userClubsRepository: UserClubsRepository,
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val clubId: String = savedStateHandle[CLUB_ID]!!

    val club = clubsRepository.getClub(clubId)

    val events = Transformations.map(
        combineTuple(occurrencesRepository.events, clubsRepository.getClubEvents(clubId))
    ) {
        Sorting.sortSavedEvents(it.first, it.second)
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