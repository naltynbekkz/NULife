package com.naltynbekkz.nulife.model

import android.net.Uri
import com.naltynbekkz.nulife.R

class LocalFile(
    var uri: Uri,
    var name: String,
    var suffix: String
) {
    constructor(uri: Uri, name: String) : this(
        uri = uri,
        name = name,
        suffix = name.substring(name.lastIndexOf('.') + 1)
    )

    fun getIcon(): Int {
        return when (suffix) {
            DOC -> R.drawable.ic_word
            DOCX -> R.drawable.ic_word
            PDF -> R.drawable.ic_adobe
            else -> R.drawable.ic_file
        }
    }

    companion object {
        const val DOC = "doc"
        const val DOCX = "docx"
        const val PDF = "pdf"
    }
}
