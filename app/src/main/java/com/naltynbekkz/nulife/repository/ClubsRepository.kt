package com.naltynbekkz.nulife.repository

import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.di.main.clubs.ClubsScope
import com.naltynbekkz.nulife.model.Club
import com.naltynbekkz.nulife.model.Event
import com.naltynbekkz.nulife.model.UserClub
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import javax.inject.Inject

@ClubsScope
class ClubsRepository @Inject constructor(val database: FirebaseDatabase) {

    val data = Transformations.map(
        FirebaseQueryLiveData(database.getReference("all_clubs"))
    ) {
        UserClub.getData(it)
    }

    fun getClub(id: String) = Transformations.map(
        FirebaseQueryLiveData(database.getReference("clubs").child(id))
    ) {
        Club(it)
    }

    fun getClubEvents(id: String) = Transformations.map(
        FirebaseQueryLiveData(database.getReference("events").orderByChild("club/id").equalTo(id))
    ) {
        Event.getData(it)
    }

}