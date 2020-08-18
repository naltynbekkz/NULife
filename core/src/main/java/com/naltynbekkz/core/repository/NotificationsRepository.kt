package com.naltynbekkz.core.repository

import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.core.FirebaseQueryLiveData
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class NotificationsRepository @Inject constructor(auth: FirebaseAuth, database: FirebaseDatabase) {

    private val reference = database.getReference("users").child(auth.uid!!).child("notifications")

    val data = Transformations.map(
        FirebaseQueryLiveData(reference)
    ) { ds ->
        HashSet<String>().apply {
            ds.children.forEach {
                add(it.key!!)
            }
        }
    }

    fun follow(
        id: String,
        type: String,
        success: (() -> Boolean)? = null,
        fail: (() -> Unit)? = null
    ) {
        reference.child(id)
            .setValue(type)
            .addOnSuccessListener {
                success?.let {
                    it()
                }
            }
            .addOnFailureListener {
                fail?.let {
                    it()
                }
            }
    }

    fun unFollow(
        id: String
    ) {
        reference.child(id).removeValue()
    }

}