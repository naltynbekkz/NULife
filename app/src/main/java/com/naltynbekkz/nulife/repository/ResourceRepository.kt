package com.naltynbekkz.nulife.repository

import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.naltynbekkz.nulife.database.ResourcesDao
import com.naltynbekkz.nulife.di.main.courses.CoursesScope
import com.naltynbekkz.nulife.model.LocalFile
import com.naltynbekkz.nulife.model.Resource
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.util.FirebaseQueryLiveData
import javax.inject.Inject

@CoursesScope
class ResourceRepository @Inject constructor(
    val resourcesDao: ResourcesDao,
    val database: FirebaseDatabase,
    val storage: FirebaseStorage
) {
    val savedResources: LiveData<List<Resource>> = resourcesDao.getAll()

    fun getResources(userCourse: UserCourse) = Transformations.map(
        FirebaseQueryLiveData(
            database.getReference("resources").child(userCourse.id)
        )
    ) {
        Resource.getData(userCourse, it)
    }

    // files
    fun post(
        resource: Resource,
        userCourse: UserCourse,
        files: ArrayList<LocalFile>,
        success: () -> Unit,
        failure: () -> Unit,
        done: (Int) -> Unit
    ) {
        val resourceReference = database.getReference("resources").child(userCourse.id).push()

        lateinit var upload: (Int) -> Unit
        upload = fun(i: Int) {
            if (i == files.size) {
                resourceReference
                    .setValue(resource.toHashMap())
                    .addOnSuccessListener {
                        success()
                    }.addOnFailureListener {
                        failure()
                    }
                return
            }

            val ref = storage.reference.child("resources")
                .child(resourceReference.key!!)
                .child(resource.getFileName())

            ref.putFile(files[i].uri).continueWithTask {
                ref.downloadUrl
            }.addOnSuccessListener {
                resource.urls.add(it.toString())
                done(i)
                upload(i + 1)
            }.addOnFailureListener {
                failure()
            }
        }
        upload(0)
    }

    // Images
    fun post(
        resource: Resource,
        userCourse: UserCourse,
        success: () -> Unit,
        failure: () -> Unit,
        images: ArrayList<Uri>,
        compress: (Uri) -> ByteArray,
        done: (Int) -> Unit
    ) {

        val resourceReference = database.getReference("resources").child(userCourse.id).push()

        lateinit var upload: (Int) -> Unit
        upload = fun(i: Int) {
            if (i == images.size) {
                resourceReference
                    .setValue(resource.toHashMap())
                    .addOnSuccessListener {
                        success()
                    }.addOnFailureListener {
                        failure()
                    }
                return
            }

            val byteArray = compress(images[i])

            val ref = storage.reference.child("resources")
                .child(resourceReference.key!!)
                .child("${resource.getFileName()}-${i}")

            ref.putBytes(byteArray).continueWithTask {
                ref.downloadUrl
            }.addOnSuccessListener {
                resource.urls.add(it.toString())
                done(i)
                upload(i + 1)
            }.addOnFailureListener {
                failure()
            }
        }
        upload(0)
    }

    @WorkerThread
    suspend fun update(resource: Resource) {
        resourcesDao.update(resource)
    }

    @WorkerThread
    suspend fun insert(resource: Resource) {
        resourcesDao.insert(resource)
    }

    @WorkerThread
    suspend fun delete(resource: Resource) {
        resourcesDao.delete(resource)
    }

}