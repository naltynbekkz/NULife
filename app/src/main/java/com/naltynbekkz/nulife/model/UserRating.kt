package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot

class UserRating(
    var ratings: ArrayList<ItemRating> = ArrayList()
) {
    constructor(ds: DataSnapshot) : this() {
        ds.children.forEach {
            ratings.add(ItemRating(it))
        }
    }

    private fun sum(): Long {
        var sum = 0L
        ratings.forEach {
            sum += it.sum()
        }
        return sum
    }

    private fun count(): Int {
        var count = 0
        ratings.forEach {
            count += it.count()
        }
        return count
    }

    fun getRating(): Float {
        return sum().toFloat() / count()
    }

}