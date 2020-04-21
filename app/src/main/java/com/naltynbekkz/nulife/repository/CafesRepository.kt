package com.naltynbekkz.nulife.repository

import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.di.main.food.FoodScope
import com.naltynbekkz.nulife.model.Cafe
import com.naltynbekkz.nulife.model.Meal
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import javax.inject.Inject

//@FoodScope
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