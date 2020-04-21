package com.naltynbekkz.nulife.repository

import android.net.Uri
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.naltynbekkz.nulife.di.main.MainScope
import com.naltynbekkz.nulife.model.User
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import com.naltynbekkz.nulife.util.ImageCompressor
import javax.inject.Inject

@MainScope
class UserRepository @Inject constructor(
    auth: FirebaseAuth,
    database: FirebaseDatabase,
    storage: FirebaseStorage,
    val imageCompressor: ImageCompressor
) {

    private val reference = database.getReference("users").child(auth.uid!!).child("info")

    private val storageReference = storage.reference.child("users").child(auth.uid!!)

    val user = Transformations.map(
        FirebaseQueryLiveData(reference)
    ) {
        User(auth.currentUser!!, it)
    }

    private fun post(
        user: User,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        reference.updateChildren(
            user.toEditHashMap()
        ).addOnSuccessListener {
            success()
        }.addOnFailureListener {
            failure()
        }
    }

    fun editProfile(
        user: User,
        image: Uri?,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        image?.let { uri ->
            val byteArray = imageCompressor.compress(uri)

            storageReference.putBytes(byteArray).continueWithTask {
                storageReference.downloadUrl
            }.addOnSuccessListener {
                user.image = it.toString()
                post(user, success, failure)

            }.addOnFailureListener {
                failure()
            }
        }
        post(user, success, failure)
    }

}