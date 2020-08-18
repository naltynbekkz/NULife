package com.naltynbekkz.clubs.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.naltynbekkz.clubs.repository.EventsRepository
import com.naltynbekkz.timetable.repository.UserClubsRepository
import com.naltynbekkz.timetable.repository.OccurrencesRepository
import com.zhuinden.livedatacombinetuplekt.combineTuple

class EventsViewModel @ViewModelInject constructor(
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