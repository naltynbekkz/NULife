package com.naltynbekkz.nulife.repository

import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.di.main.market.MarketScope
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.model.UserRating
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import javax.inject.Inject

//@MarketScope
class UserRatingsRepository @Inject constructor(
    val auth: FirebaseAuth,
    val database: FirebaseDatabase
) {

    fun getAuthorRating(id: String) = Transformations.map(
        FirebaseQueryLiveData(database.getReference("user_ratings").child(id))
    ) {
        UserRating(it)
    }

    fun getMyRating(item: Item) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("user_ratings").child(item.author.id).child(item.id)
                .child(auth.uid!!)
        )
    ) {
        it.value as Long?
    }

    fun rate(
        rating: Long,
        item: Item,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        database.getReference("user_ratings").child(item.author.id).child(item.id)
            .child(auth.uid!!)
            .setValue(rating)
            .addOnSuccessListener { success() }
            .addOnFailureListener { failure() }
    }

}