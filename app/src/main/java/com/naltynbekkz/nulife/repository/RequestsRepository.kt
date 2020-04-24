package com.naltynbekkz.nulife.repository

import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.di.main.market.MarketScope
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.model.Request
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import javax.inject.Inject

@MarketScope
class RequestsRepository @Inject constructor(
    val auth: FirebaseAuth,
    database: FirebaseDatabase
) {

    private val reference = database.getReference("requests")

    fun getRequests(id: String) = Transformations.map(
        FirebaseQueryLiveData(reference.child(id))
    ) {
        Request.getData(it)
    }

    fun getRequest(item: Item) = Transformations.map(
        FirebaseQueryLiveData(reference.child(item.id).child(auth.uid!!))
    ) {
        if (it.exists()) Request(it) else null
    }

    fun request(
        item: Item,
        request: Request,
        success: () -> Unit
    ) {
        request.name = auth.currentUser!!.displayName!!
        reference
            .child(item.id)
            .child(auth.uid!!)
            .setValue(
                request.toHashMap()
            )
            .addOnSuccessListener {
                success()
            }
    }

    fun removeRequest(item: Item) {
        reference.child(item.id).child(auth.uid!!)
            .removeValue()
    }

}