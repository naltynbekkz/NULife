package com.naltynbekkz.nulife.model

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class Vote(
    var uid: String = "",
    var vote: Boolean = false
) : Serializable {

    constructor(ds: DataSnapshot) : this(ds.key!!, ds.child("vote").value as Boolean)

    companion object{
        fun dataToVotes(ds: DataSnapshot): ArrayList<Vote> {
            return ArrayList<Vote>().apply {
                ds.children.forEach {
                    add(Vote(it))
                }
            }
        }
    }

}