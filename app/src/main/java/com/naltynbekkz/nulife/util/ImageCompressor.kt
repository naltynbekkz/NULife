package com.naltynbekkz.nulife.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

class ImageCompressor(private val contentResolver: ContentResolver) {

    fun compress(uri: Uri): ByteArray {
        @Suppress("DEPRECATION")
        var bitmap =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        contentResolver,
                        uri
                    )
                )
            } else {
                MediaStore.Images.Media.getBitmap(
                    contentResolver,
                    uri
                )
            }
        var height = 1280
        var width = 1280
        if (bitmap.height > bitmap.width) {
            width = (bitmap.width * 1280) / bitmap.height
        } else {
            height = (bitmap.height * 1280) / bitmap.width
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)


        var byteArray: ByteArray
        var currentQuality = 100
        do {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, baos)
            byteArray = baos.toByteArray()
            currentQuality -= 5
        } while (byteArray.size >= 100 * 1024 && currentQuality >= 0)
        return byteArray
    }

    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: ImageCompressor? = null

        fun getInstance(contentResolver: ContentResolver) =
            instance ?: synchronized(this) {
                instance ?: ImageCompressor(contentResolver).also { instance = it }
            }
    }

}