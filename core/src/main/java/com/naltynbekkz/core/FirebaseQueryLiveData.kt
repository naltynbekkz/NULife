package com.naltynbekkz.core

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.Query


class FirebaseQueryLiveData(private val query: Query) : LiveData<DataSnapshot>() {
    private val listener = query.value {
        value = it
    }

    override fun onActive() {
        query.addValueEventListener(listener)
    }

    override fun onInactive() {
        query.removeEventListener(listener)
    }

}

class FirebaseSingleQueryLiveData(query: Query) : LiveData<DataSnapshot>() {
    private val listener = query.single {
        value = it
    }
}