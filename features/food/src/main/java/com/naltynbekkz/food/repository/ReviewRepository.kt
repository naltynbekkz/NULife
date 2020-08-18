package com.naltynbekkz.food.repository

import android.net.Uri
import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.naltynbekkz.core.FirebaseQueryLiveData
import com.naltynbekkz.core.ImageCompressor
import com.naltynbekkz.food.model.Review
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ReviewRepository @Inject constructor(
    val auth: FirebaseAuth,
    val database: FirebaseDatabase,
    val storage: FirebaseStorage,
    val imageCompressor: ImageCompressor
) {

    fun getReviews(cafeId: String) = Transformations.map(
        FirebaseQueryLiveData(database.getReference("cafes").child(cafeId).child("reviews"))
    ) {
        Review.getData(it)
    }

    fun post(
        cafeId: String,
        review: Review,
        success: () -> Boolean,
        failure: () -> Unit,
        images: ArrayList<Uri>,
        callback: (Int) -> Unit
    ) {

        lateinit var upload: (Int) -> Unit
        upload = fun(i: Int) {
            if (i == images.size) {
                database.getReference("cafes").child(cafeId).child("reviews").child(auth.uid!!)
                    .setValue(review.toHashMap())
                    .addOnSuccessListener {
                        success()
                    }.addOnFailureListener {
                        failure()
                    }
                return
            }

            val byteArray = imageCompressor.compress(images[i])

            val ref = storage.reference.child("reviews").child(cafeId).child(auth.uid!!)
                .child(i.toString())

            ref.putBytes(byteArray).continueWithTask {
                ref.downloadUrl
            }.addOnSuccessListener {
                review.image = it.toString()
                callback(i)
                upload(i + 1)
            }.addOnFailureListener {
                failure()
            }
        }
        upload(0)
    }

}