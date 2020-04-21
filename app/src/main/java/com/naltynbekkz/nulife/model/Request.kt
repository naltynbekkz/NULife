package com.naltynbekkz.nulife.model

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot

class Request(
    var name: String = "",
    var details: String = "",
    var contacts: ArrayList<Contact> = ArrayList()
) {

    constructor(ds: DataSnapshot) : this(
        name = ds.child("name").value as String,
        details = ds.child("details").value as String,
        contacts = Contact.getList(ds.child("contacts"))
    )

    constructor(user: FirebaseUser) : this(
        name = user.displayName!!
    )

    companion object {
        fun getData(ds: DataSnapshot): ArrayList<Request> {
            return ArrayList<Request>().apply {
                ds.children.forEach {
                    add(Request(it))
                }
            }
        }
    }

    fun toHashMap(): HashMap<String, Any> {
        return HashMap<String, Any>().apply {
            put("name", name)
            put("details", details)
            put("contacts", Contact.toHashMap(contacts))
        }
    }

}