package com.naltynbekkz.nulife.ui.clubs.viewmodel

import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.repository.ClubsRepository
import com.naltynbekkz.nulife.repository.UserClubsRepository
import javax.inject.Inject

class ClubsViewModel @Inject constructor(
    clubsRepository: ClubsRepository,
    userClubsRepository: UserClubsRepository
) : ViewModel() {

    val allClubs = clubsRepository.data
    val myClubs = userClubsRepository.userClubs

}