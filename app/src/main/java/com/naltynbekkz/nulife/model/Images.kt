package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class Images(
    var standard: String,
    var cafe: String?,
    var eventMain: String?
) : Serializable {
    constructor(ds: DataSnapshot) : this(
        standard = ds.child("standard").value as String,
        cafe = ds.child("cafe").value as String?,
        eventMain = ds.child("event_main").value as String?
    )
}