package com.naltynbekkz.core

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

fun Query.single(onComplete: (DataSnapshot) -> Unit) {
    addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(p0: DataSnapshot) {
            try {
                onComplete(p0)
            } catch (e: Exception) {
                Log.d("FirebaseReference", "Datasnapshot processing problems")
            }
        }
    })
}

fun Query.value(onComplete: (DataSnapshot) -> Unit): ValueEventListener {
    return addValueEventListener(object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(p0: DataSnapshot) {
            try {
                onComplete(p0)
            } catch (e: Exception) {
                Log.d("FirebaseReference", "DataSnapshot processing problems")
            }
        }

    })
}