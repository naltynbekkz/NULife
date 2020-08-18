package com.naltynbekkz.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.DataSnapshot
import java.io.Serializable

@Entity(tableName = "contacts")
class Contact(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var data: String = "",
    var type: String = ""
) : Serializable {
    constructor(ds: DataSnapshot) : this(
        data = ds.child("data").value as String,
        type = ds.child("type").value as String
    )

    fun getIcon() = when (type) {
        LOCATION -> R.drawable.ic_location
        PHONE -> R.drawable.ic_phone
        VK -> R.drawable.ic_vk
        TELEGRAM -> R.drawable.ic_telegram
        EMAIL -> R.drawable.ic_mail
        INSTAGRAM -> R.drawable.ic_instagram
        FACEBOOK -> R.drawable.ic_facebook
        else -> R.drawable.ic_link
    }

    fun toHashMap(): HashMap<String, String> {
        return HashMap<String, String>().apply {
            put("data", data)
            put("type", type)
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other != null && other is Contact) {
            id == other.id && data == other.data && type == other.type
        } else false
    }

    companion object {

        fun getList(ds: DataSnapshot): ArrayList<Contact> {
            return ArrayList<Contact>().apply {
                ds.children.forEach {
                    add(Contact(it))
                }
            }
        }

        fun toHashMap(contacts: ArrayList<Contact>): HashMap<String, Any> {
            return HashMap<String, Any>().apply {
                for (i in contacts.indices) {
                    put("$i", contacts[i].toHashMap())
                }
            }
        }

        const val LOCATION = "location"
        const val PHONE = "phone"
        const val VK = "vk"
        const val TELEGRAM = "telegram"
        const val EMAIL = "email"
        const val INSTAGRAM = "instagram"
        const val FACEBOOK = "facebook"
        const val LINK = "link"
    }
}
