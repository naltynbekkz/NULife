package com.naltynbekkz.food.model

import com.google.firebase.database.DataSnapshot
import com.naltynbekkz.core.Convert
import com.naltynbekkz.core.Images

class Meal(
    var id: String,
    var title: String,
    var price: Long,
    var details: String?,
    var type: String,
    var days: ArrayList<Day>? = null,
    var lastAvailable: Long,
    var discountedPrice: Long,
    var images: Images
) {

    fun getDiscount(): Int {
        return ((price - discountedPrice) * 100 / price).toInt()
    }

    fun isAvailable(): Boolean {
        if (days != null) {
            val day = days!![Convert.getDayOfWeek()]
            val seconds = Convert.getSeconds()
            if (seconds <= day.end && seconds >= day.start) {
                return true
            }
        }
        return System.currentTimeMillis() / 1000 - lastAvailable < 12 * 60 * 60

    }

    constructor(ds: DataSnapshot) : this(
        id = ds.key!!,
        title = ds.child("title").value as String,
        price = ds.child("price").value as Long,
        details = ds.child("details").value as String?,
        type = ds.child("type").value as String,
        lastAvailable = ds.child("last_available").value as Long,
        discountedPrice = ds.child("discounted_price").value as Long,
        images = Images(ds.child("urls"))
    ) {
        if (ds.hasChild("days")) {
            days = Day.getList(ds.child("days"))
        }
    }

    companion object {
        fun getData(ds: DataSnapshot): ArrayList<Meal> {
            return ArrayList<Meal>().apply {
                ds.children.forEach {
                    add(Meal(it))
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Meal) {
            id == other.id && title == other.title && details == other.details && price == other.price && type == other.type && days == other.days && discountedPrice == other.discountedPrice && lastAvailable == other.lastAvailable && images == other.images
        } else false
    }

}