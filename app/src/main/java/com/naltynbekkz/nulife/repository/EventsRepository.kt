package com.naltynbekkz.nulife.repository

import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.di.main.clubs.ClubsScope
import com.naltynbekkz.nulife.model.Event
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import javax.inject.Inject

@ClubsScope
class EventsRepository @Inject constructor(val database: FirebaseDatabase) {

    val data = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("events").orderByChild("start")
                .startAt((System.currentTimeMillis() / 1000).toDouble())
        )
    ) {
        Event.getData(it)
    }

    fun getEvent(id: String) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("events").child(id)
        )
    ) {
        Event(it)
    }

}