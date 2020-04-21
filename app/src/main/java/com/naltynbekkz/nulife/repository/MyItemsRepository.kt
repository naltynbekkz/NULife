package com.naltynbekkz.nulife.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.di.main.market.MarketScope
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.util.value
import javax.inject.Inject

//@MarketScope
class MyItemsRepository @Inject constructor(
    database: FirebaseDatabase,
    userRepository: UserRepository
) {

    val user = userRepository.user

    // TODO: refactor

    private val othersPublicDatabaseReference =
        database.getReference("items").orderByChild("author/uid").equalTo(user.value!!.uid)
    private val othersPrivateDatabaseReference =
        database.getReference("items").orderByChild("author/uid").equalTo(user.value!!.anonymousId)
    private val femalePublicDatabaseReference =
        database.getReference("female_items").orderByChild("author/uid").equalTo(user.value!!.uid)
    private val femalePrivateDatabaseReference =
        database.getReference("female_items").orderByChild("author/uid")
            .equalTo(user.value!!.anonymousId)


    private val _data = MutableLiveData<ArrayList<Item>>()
    val data: LiveData<ArrayList<Item>>
        get() = _data


    private var femalePrivateItems = ArrayList<Item>()
    private var femalePublicItems = ArrayList<Item>()
    private var otherPrivateItems = ArrayList<Item>()
    private var otherPublicItems = ArrayList<Item>()

    init {
        othersPublicDatabaseReference.value {
            otherPublicItems = Item.getData(false, it)
            postItems()
        }
        othersPrivateDatabaseReference.value {
            otherPrivateItems = Item.getData(false, it)
            postItems()
        }
        femalePrivateDatabaseReference.value {
            femalePrivateItems = Item.getData(true, it)
            postItems()
        }
        femalePublicDatabaseReference.value {
            femalePublicItems = Item.getData(true, it)
            postItems()
        }
    }

    private fun postItems() {
        _data.postValue(ArrayList<Item>().apply {
            addAll(femalePrivateItems)
            addAll(femalePublicItems)
            addAll(otherPrivateItems)
            addAll(otherPublicItems)
            sort()
        })
    }

}