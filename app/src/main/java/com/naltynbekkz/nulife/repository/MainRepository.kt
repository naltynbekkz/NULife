package com.naltynbekkz.nulife.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MainRepository @Inject constructor(
    private val auth: FirebaseAuth,
    val database: FirebaseDatabase,
    private val instanceId: FirebaseInstanceId
) {

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
    }

    fun isSignedIn() = auth.currentUser == null

    fun updateInstanceId() {
        auth.uid?.let {
            instanceId.instanceId.addOnSuccessListener { token ->
                database.getReference("users").child(auth.uid!!).child("info").child("token_id")
                    .setValue(token)
            }
        }
    }

}