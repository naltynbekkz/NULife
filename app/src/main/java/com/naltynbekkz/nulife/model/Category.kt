package com.naltynbekkz.nulife.model

import com.naltynbekkz.nulife.R

class Category(
    var id: String,
    var following: Boolean? = false
) {
    var icon: Int = when (id) {
        "Services" -> R.drawable.ic_services
        "Jobs" -> R.drawable.ic_jobs
        "Transport" -> R.drawable.ic_transport
        "Clothes" -> R.drawable.ic_clothes
        "Electronics" -> R.drawable.ic_electronics
        "Hobby" -> R.drawable.ic_hobby
        "Beauty&care" -> R.drawable.ic_beauty
        "Books" -> R.drawable.ic_books
        "Food" -> R.drawable.ic_food
        "Home" -> R.drawable.ic_home
        "Kitchen" -> R.drawable.ic_kitchen
        "Others" -> R.drawable.ic_others
        "Buy" -> R.drawable.ic_buy
        "Female" -> R.drawable.ic_female
        "Free" -> R.drawable.ic_free
        else -> R.drawable.ic_free
    }

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Category) {
            id == other.id && following == other.following
        } else false
    }

}