package com.naltynbekkz.nulife.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.DataSnapshot
import com.naltynbekkz.nulife.ui.market.search.Filter
import com.naltynbekkz.nulife.util.Convert
import java.io.Serializable

@Entity(tableName = "items")
class Item(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var title: String = "",
    var author: Student = Student(),
    var contacts: ArrayList<Contact> = ArrayList(),
    var details: String = "",
    var price: Long = 0,
    var timestamp: Long = 0,
    @ColumnInfo(name = "discounted_price")
    var discountedPrice: Long = 0,
    @ColumnInfo(name = "last_active")
    var lastActive: Long = 0,
    var images: ArrayList<String> = ArrayList(),
    var category: String = "",
    var sell: Boolean = true,
    var female: Boolean = false
) : Serializable, Comparable<Item> {

    constructor(female: Boolean, ds: DataSnapshot) : this(
        id = ds.key!!,
        title = ds.child("title").value as String,
        author = Student(ds.child("author")),
        contacts = Contact.getList(ds.child("contacts")),
        details = ds.child("details").value as String,
        price = ds.child("price").value as Long,
        discountedPrice = ds.child("discounted_price").value as Long,
        timestamp = ds.child("timestamp").value as Long,
        lastActive = ds.child("last_active").value as Long,
        sell = ds.child("sell").value as Boolean,
        female = female,
        category = ds.child("category").value as String
    ) {
        ds.child("urls").children.forEach {
            images.add(it.value as String)
        }
    }

    companion object {
        fun getData(female: Boolean, dataSnapshot: DataSnapshot): ArrayList<Item> {
            return ArrayList<Item>().apply {
                dataSnapshot.children.forEach {
                    add(Item(female, it))
                }
                reverse()
            }
        }
    }

    fun fits(filter: Filter): Boolean {
        return when {
            filter.female && !female -> false
            filter.sell != null && filter.sell!! != sell -> false
            filter.category != null && filter.category != category -> false
            else -> true
        }

    }

    fun getDiscount(): Int {
        return ((price - discountedPrice) * 100 / price).toInt()
    }

    override fun compareTo(other: Item): Int {
        return (lastActive - other.lastActive).toInt()
    }

    fun toHashMap(): HashMap<String, Any?> {
        return HashMap<String, Any?>().apply {
            put("title", title)
            put("author", author.toHashMap())
            put("contacts", Contact.toHashMap(contacts))
            put("details", details)
            put("price", price)
            put("discounted_price", discountedPrice)
            put("timestamp", timestamp)
            put("last_active", lastActive)
            put("urls", imagesHashMap())
            put("sell", sell)
            put("category", category)
        }
    }

    fun isNew() = id.isEmpty()

    fun isValid(): Boolean {
        return title.isNotEmpty() && category.isNotEmpty()
    }

    fun getTimePast(): String {
        return Convert.timestampToTimePast(lastActive)
    }

    private fun imagesHashMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            for (i in images.indices) {
                put("$i", images[i])
            }
        }
    }

}