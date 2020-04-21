package com.naltynbekkz.nulife.repository

import androidx.lifecycle.Transformations
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.di.main.MainScope
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import javax.inject.Inject

//@MainScope
class FemaleRepository @Inject constructor(auth: FirebaseAuth, database: FirebaseDatabase) {

    private val reference = database.getReference("female_requests").child(auth.uid!!)

    val data = Transformations.map(
        FirebaseQueryLiveData(reference)
    ) {
        it.value as Boolean?
    }

    fun applyFemale() {
        reference.setValue(true)
    }

}