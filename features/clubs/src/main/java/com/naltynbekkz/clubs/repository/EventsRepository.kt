package com.naltynbekkz.clubs.repository

import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.clubs.model.Event
import com.naltynbekkz.core.FirebaseQueryLiveData
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class EventsRepository @Inject constructor(val database: FirebaseDatabase) {

    val data = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("events").orderByChild("start")
                .startAt((System.currentTimeMillis() / 1000).toDouble())
        )
    ) {
        com.naltynbekkz.clubs.model.Event.getData(it)
    }

    fun getEvent(id: String) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("events").child(id)
        )
    ) {
        com.naltynbekkz.clubs.model.Event(it)
    }

}