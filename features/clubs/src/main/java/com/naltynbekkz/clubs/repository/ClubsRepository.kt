package com.naltynbekkz.clubs.repository

import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.core.FirebaseQueryLiveData
import com.naltynbekkz.timetable.model.UserClub
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ClubsRepository @Inject constructor(val database: FirebaseDatabase) {

    val data = Transformations.map(
        FirebaseQueryLiveData(database.getReference("all_clubs"))
    ) {
        UserClub.getData(it)
    }

    fun getClub(id: String) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("clubs").child(id)
        )
    ) {
        com.naltynbekkz.clubs.model.Club(it)
    }

    fun getClubEvents(id: String) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("events").orderByChild("club/id").equalTo(id)
        )
    ) {
        com.naltynbekkz.clubs.model.Event.getData(it)
    }

}