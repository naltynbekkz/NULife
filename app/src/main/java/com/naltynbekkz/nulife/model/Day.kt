package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class Day(
    var start: Long,
    var end: Long
) : Serializable {
    constructor(ds: DataSnapshot) : this(
        start = ds.child("start").value as Long,
        end = ds.child("end").value as Long
    )

    companion object {
        fun getList(ds: DataSnapshot): ArrayList<Day> {
            return ArrayList<Day>().apply {
                arrayListOf("mon", "tue", "wed", "thu", "fri", "sat", "sun").forEach {
                    add(Day(ds.child(it)))
                }
            }
        }
    }
}