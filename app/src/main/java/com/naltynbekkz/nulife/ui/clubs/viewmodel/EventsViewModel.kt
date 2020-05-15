package com.naltynbekkz.nulife.ui.clubs.viewmodel

import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.repository.EventsRepository
import com.naltynbekkz.nulife.repository.OccurrencesRepository
import com.naltynbekkz.nulife.repository.UserClubsRepository
import com.zhuinden.livedatacombinetuplekt.combineTuple
import javax.inject.Inject

class EventsViewModel @Inject constructor(
    occurrencesRepository: OccurrencesRepository,
    userClubsRepository: UserClubsRepository,
    eventsRepository: EventsRepository
) : ViewModel() {

    val events = combineTuple(
        occurrencesRepository.events,
        userClubsRepository.userClubs,
        eventsRepository.data
    )

    val tasks = occurrencesRepository.events

    val userClubs = userClubsRepository.userClubs

    val allEvents = eventsRepository.data


}