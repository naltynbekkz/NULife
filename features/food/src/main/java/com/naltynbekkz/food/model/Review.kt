package com.naltynbekkz.food.model

import com.google.firebase.database.DataSnapshot
import com.naltynbekkz.core.Convert
import com.naltynbekkz.food.R
import java.io.Serializable

class Review(
    var id: String = "",
    var name: String = "",
    var rating: Long = 0,
    var body: String? = null,
    var timestamp: Long = System.currentTimeMillis() / 1000,
    var image: String? = null
) : Serializable {

    constructor(ds: DataSnapshot) : this(
        id = ds.key!!,
        name = ds.child("name").value as String,
        rating = ds.child("rating").value as Long,
        body = ds.child("body").value as String?,
        timestamp = ds.child("timestamp").value as Long,
        image = ds.child("image").value as String?
    )

    fun getRating(): Float = rating.toFloat()

    fun getTime() = Convert.timestampToTimePast(timestamp)

    fun toHashMap(): HashMap<String, Any?> {
        return HashMap<String, Any?>().apply {
            put("name", name)
            put("rating", rating)
            put("body", body)
            put("timestamp", System.currentTimeMillis() / 1000)
            put("image", image)
        }
    }

    fun getBodyVisibility() = !body.isNullOrEmpty()

    fun getImageVisibility() = !image.isNullOrEmpty()

    fun getRatingText(): Int =
        when (rating) {
            1L -> R.string.hated_it
            2L -> R.string.disliked_it
            3L -> R.string.its_okay
            4L -> R.string.liked_it
            5L -> R.string.loved_it
            else -> R.string.rate_and_review
        }

    companion object {
        fun getData(ds: DataSnapshot): ArrayList<Review> {
            return ArrayList<Review>().apply {
                ds.children.forEach {
                    add(Review(it))
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Review) {
            id == other.id && name == other.name && rating == other.rating && body == other.body && timestamp == other.timestamp && image == other.image
        } else false
    }

}