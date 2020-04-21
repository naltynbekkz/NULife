package com.naltynbekkz.nulife.ui.clubs.viewmodel

import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.repository.EventsRepository
import com.naltynbekkz.nulife.repository.OccurrencesRepository
import com.naltynbekkz.nulife.repository.UserClubsRepository
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    occurrencesRepository: OccurrencesRepository,
    userClubsRepository: UserClubsRepository,
    eventsRepository: EventsRepository
) : ViewModel() {

    val tasks = occurrencesRepository.events

    val userClubs = userClubsRepository.userClubs

    val allEvents = eventsRepository.data

}