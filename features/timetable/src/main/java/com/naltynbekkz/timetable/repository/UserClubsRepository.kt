package com.naltynbekkz.timetable.repository

import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.core.FirebaseQueryLiveData
import com.naltynbekkz.timetable.model.UserClub
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UserClubsRepository @Inject constructor(auth: FirebaseAuth, database: FirebaseDatabase) {

    private val reference =
        database.getReference("users").child(auth.uid!!).child("clubs")

    val userClubs = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("users").child(auth.uid!!).child("clubs")
        )
    ) {
        UserClub.getData(it)
    }

    fun follow(userClub: UserClub, callback: () -> Unit) {
        reference.child(userClub.id)
            .setValue(userClub.toHashMap())
            .addOnSuccessListener {
                callback()
            }
    }

    fun unFollow(id: String, callback: () -> Unit) {
        reference.child(id)
            .removeValue()
            .addOnSuccessListener {
                callback()
            }
    }

}