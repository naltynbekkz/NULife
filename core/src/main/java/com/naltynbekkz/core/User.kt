package com.naltynbekkz.core

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import java.io.Serializable

class User(
    var uid: String,
    var name: String,
    var email: String,
    var anonymousId: String,
    var school: String? = "",
    var major: String? = "",
    var year: Long? = 0,
    var female: Boolean,
    var image: String? = ""
) : Serializable {

    fun anonymous() = Student(anonymousId, "Anonymous", null)
    fun student() = Student(uid, name, image)

    fun toEditHashMap(): HashMap<String, Any> {
        return HashMap<String, Any>().apply {
            school?.let { put("school", school!!) }
            major?.let { put("major", major!!) }
            year?.let { put("year", year!!) }
            image?.let { put("image", image!!) }
        }
    }
//
//    companion object {
//        fun getUser(user: FirebaseUser?, ds: DataSnapshot): User? {
//            return if (user == null || !ds.exists()) null else User(user, ds)
//        }
//    }

    constructor(user: FirebaseUser, ds: DataSnapshot) : this(
        uid = user.uid,
        email = user.email!!,
        name = user.displayName!!,
        anonymousId = ds.child("anonymous_id").value as String,
        school = ds.child("school").value as String?,
        major = ds.child("major").value as String?,
        year = ds.child("year").value as Long?,
        female = ds.child("female").value as Boolean,
        image = ds.child("image").value as String?
    )
}