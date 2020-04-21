package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class Registration(
    var link: String,
    var start: Long,
    var end: Long
) : Serializable {
    constructor(ds: DataSnapshot) : this(
        link = ds.child("link").value as String,
        start = ds.child("start").value as Long,
        end = ds.child("end").value as Long
    )

    fun available(): Boolean {
        return System.currentTimeMillis() / 1000 in start until end
    }

}