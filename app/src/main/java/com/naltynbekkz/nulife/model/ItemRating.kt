package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class ItemRating(
    private val ratings: ArrayList<Pair<String, Long>> = ArrayList()
) : Serializable {
    constructor(ds: DataSnapshot) : this() {
        ds.children.forEach {
            ratings.add(Pair(it.key!!, it.value as Long))
        }
    }

    fun getRating(): Float {
        return sum().toFloat() / count()
    }

    fun sum(): Long {
        var sum = 0L
        ratings.forEach {
            sum += it.second
        }
        return sum
    }

    fun count(): Int {
        return ratings.size
    }

}