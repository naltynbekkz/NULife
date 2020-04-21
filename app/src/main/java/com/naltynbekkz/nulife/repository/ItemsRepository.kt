package com.naltynbekkz.nulife.repository

import android.net.Uri
import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.naltynbekkz.nulife.di.main.market.MarketScope
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import com.naltynbekkz.nulife.util.ImageCompressor
import javax.inject.Inject

//@MarketScope
class ItemsRepository @Inject constructor(
    database: FirebaseDatabase,
    val storage: FirebaseStorage,
    val imageCompressor: ImageCompressor
) {

    private val reference = database.getReference("items")
    private val femaleReference = database.getReference("female_items")


    val items = Transformations.map(
        FirebaseQueryLiveData(reference.orderByChild("last_active"))
    ) {
        Item.getData(false, it)
    }

    val femaleItems = Transformations.map(
        FirebaseQueryLiveData(femaleReference.orderByChild("last_active"))
    ) {
        Item.getData(true, it)
    }

    fun getItem(item: Item) = Transformations.map(
        FirebaseQueryLiveData(
            (if (!item.female) reference else femaleReference)
                .child(item.id)
        )
    ) {
        Item(item.female, it)
    }

    fun post(
        item: Item,
        success: () -> Unit,
        failure: () -> Unit,
        images: ArrayList<Uri>,
        done: (Int) -> Unit
    ) {
        val reference = (if (item.female) femaleReference else reference).push()

        lateinit var upload: (Int) -> Unit
        upload = fun(i: Int) {
            if (i == images.size) {
                reference
                    .setValue(item.toHashMap())
                    .addOnSuccessListener {
                        success()
                    }.addOnFailureListener {
                        failure()
                    }
                return
            }

            val byteArray = imageCompressor.compress(images[i])

            val ref = storage.reference.child("items")
                .child(reference.key!!)
                .child(i.toString())

            ref.putBytes(byteArray).continueWithTask {
                ref.downloadUrl
            }.addOnSuccessListener {
                item.images.add(it.toString())
                done(i)
                upload(i + 1)
            }.addOnFailureListener {
                failure()
            }
        }
        upload(0)
    }

    fun edit(
        item: Item,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        (if (item.female) femaleReference else reference)
            .child(item.id)
            .setValue(item.toHashMap())
            .addOnSuccessListener {
                success()
            }.addOnFailureListener {
                failure()
            }
    }

    fun delete(
        item: Item,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        (if (!item.female) reference else femaleReference)
            .child(item.id)
            .removeValue()
            .addOnSuccessListener {
                success()
            }.addOnFailureListener {
                failure()
            }
    }

}