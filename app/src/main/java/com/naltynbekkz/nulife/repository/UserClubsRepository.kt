package com.naltynbekkz.nulife.repository

import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.di.main.clubs.ClubsScope
import com.naltynbekkz.nulife.model.UserClub
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import javax.inject.Inject

//@ClubsScope
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