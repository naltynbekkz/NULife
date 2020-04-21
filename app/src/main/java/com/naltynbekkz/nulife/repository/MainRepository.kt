package com.naltynbekkz.nulife.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.naltynbekkz.nulife.di.main.MainScope
import javax.inject.Inject

@MainScope
class MainRepository @Inject constructor(
    val auth: FirebaseAuth,
    val database: FirebaseDatabase,
    private val instanceId: FirebaseInstanceId
) {

    fun updateInstanceId() {
        auth.uid?.let {
            instanceId.instanceId.addOnSuccessListener { token ->
                database.getReference("users").child(auth.uid!!).child("info").child("token_id")
                    .setValue(token)
            }
        }
    }

}