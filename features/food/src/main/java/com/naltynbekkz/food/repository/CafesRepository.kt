package com.naltynbekkz.food.repository

import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.core.FirebaseQueryLiveData
import com.naltynbekkz.food.model.Meal
import com.naltynbekkz.food.model.Cafe
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class CafesRepository @Inject constructor(val database: FirebaseDatabase) {

    private val reference = database.getReference("cafes")

    val cafes = Transformations.map(
        FirebaseQueryLiveData(
            reference.orderByChild("approved").equalTo(true)
        )
    ) {
        Cafe.getData(it)
    }

    fun getCafe(id: String) = Transformations.map(
        FirebaseQueryLiveData(reference.child(id))
    ) {
        Cafe(it)
    }

    fun getMeals(id: String) = Transformations.map(
        FirebaseQueryLiveData(database.getReference("meals").child(id))
    ) {
        Meal.getData(it)
    }

    fun getFeatured(cafeId: String, id: String) = Transformations.map(
        FirebaseQueryLiveData(database.getReference("meals").child(cafeId).child(id))
    ) {
        if (it.exists()) Meal(it) else null
    }

}