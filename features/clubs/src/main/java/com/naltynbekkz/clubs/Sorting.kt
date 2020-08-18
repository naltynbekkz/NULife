package com.naltynbekkz.clubs

import com.naltynbekkz.clubs.model.Event
import com.naltynbekkz.core.Convert.Companion.removeHours
import com.naltynbekkz.timetable.model.Occurrence
import com.naltynbekkz.timetable.model.UserClub
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

object Sorting {

    fun eventsToDays(events: ArrayList<Event>?): ArrayList<Map.Entry<Long, ArrayList<Event>>>? {
        return if (events != null) {
            ArrayList(TreeMap<Long, ArrayList<Event>>().apply {
                for (event in events) {
                    val day = removeHours(
                        event.start
                    ).timeInMillis / 1000
                    if (containsKey(day)) {
                        this[day]!!.add(event)
                    } else {
                        this[day] = ArrayList<Event>().apply {
                            add(event)
                        }
                    }
                }
            }.entries)
        } else {
            null
        }
    }


    fun sortMyEvents(clubs: ArrayList<UserClub>?, events: ArrayList<Event>?): ArrayList<Event> {
        val myClubs = UserClub.getHashSet(clubs)
        return ArrayList<Event>().apply {
            events?.forEach {
                if (myClubs.contains(it.club.id)) {
                    add(it)
                }
            }
        }
    }

    fun sortSavedEvents(
        saved: List<Occurrence>?,
        events: ArrayList<Event>?
    ): ArrayList<Event>? {
        val savedTasks = HashSet<String>().apply {
            saved?.forEach {
                add(it.id)
            }
        }
        events?.forEach {
            it.saved = savedTasks.contains(it.id)
        }
        return events
    }

}