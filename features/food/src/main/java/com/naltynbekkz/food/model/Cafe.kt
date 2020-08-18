package com.naltynbekkz.food.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.naltynbekkz.core.Contact
import com.naltynbekkz.core.Convert
import com.naltynbekkz.core.Convert.Companion.getDayOfWeek
import java.io.Serializable

data class Cafe(
    var id: String,
    var title: String,
    var featured: String?,
    var contacts: ArrayList<Contact>,
    var days: ArrayList<Day>,
    var details: String?,
    var logo: String,
    var reviews: ArrayList<Review>
) : Serializable {

    val ratings = intArrayOf(0, 0, 0, 0, 0)

    init {
        reviews.forEach {
            ratings[(it.rating - 1).toInt()]++
        }
    }

    val rating: Float
        get() {
            if (reviews.size == 0) {
                return 0f
            }
            var sum = 0f
            reviews.forEach {
                sum += it.rating
            }
            return sum / reviews.size
        }

    constructor(ds: DataSnapshot) : this(
        id = ds.key!!,
        title = ds.child("title").value as String,
        featured = ds.child("featured").value as String?,
        contacts = Contact.getList(ds.child("contacts")),
        days = Day.getList(ds.child("days")),
        details = ds.child("details").value as String?,
        logo = ds.child("urls").child("logo").value as String,
        reviews = Review.getData(ds.child("reviews"))
    )

    fun myRating(): Float? {
        val uid = FirebaseAuth.getInstance().uid
        reviews.forEach {
            if (it.id == uid) {
                return it.rating.toFloat()
            }
        }
        return null
    }

    fun opensAt(): String {
        val currentTime = Convert.getSeconds()
        val dayOfWeek = getDayOfWeek()

        return if (currentTime < days[dayOfWeek].start) {
            "Opens at " + Convert.secondsToTime(days[dayOfWeek].start)
        } else if (currentTime < days[dayOfWeek].end) {
            if (days[dayOfWeek].end - currentTime < 60 * 60) {
                "Closes at " + Convert.secondsToTime(days[dayOfWeek].end)
            } else {
                "Open"
            }
        } else if (days[(dayOfWeek + 1) % 7].start != 0L) {
            "Opens tomorrow at " + Convert.secondsToTime(days[(dayOfWeek + 1) % 7].start)
        } else {
            "Closed"
        }
    }

    fun isOpen(): Boolean {
        val currentTime = (System.currentTimeMillis() / 1000 + 6 * 60 * 60) % (24 * 60 * 60)
        val dayOfWeek =
            (((System.currentTimeMillis() / 1000 + 6 * 60 * 60) / (24 * 60 * 60) + 3) % 7).toInt()
        return days[dayOfWeek].start < currentTime && currentTime < days[dayOfWeek].end
    }

    fun getHours(): String {
        val hours = StringBuilder()

        days.forEach {
            if (it.start == it.end) {
                hours.append("closed")
            } else {
                hours.append(Convert.secondsToTime(it.start))
                hours.append(" - ")
                hours.append(Convert.secondsToTime(it.end))
            }
            hours.append("\n")
        }
        hours.delete(hours.length - 1, hours.length)
        return hours.toString()
    }

    fun getCount(i: Int): String {
        var s = 0
        reviews.forEach {
            if (it.rating.toInt() == i) {
                s++
            }
        }
        return s.toString()
    }

    fun barSize(i: Int): Int {
        val array = arrayOf(0, 0, 0, 0, 0)
        reviews.forEach {
            array[it.rating.toInt() - 1]++
        }
        val max = array.max()!!
        return if (max != 0) Convert.dpToPx(112 * array[i] / max) else 0
    }

    companion object {
        fun getData(ds: DataSnapshot): ArrayList<Cafe> {
            return ArrayList<Cafe>().apply {
                ds.children.forEach {
                    add(Cafe(it))
                }
            }
        }
    }

}