package com.naltynbekkz.courses.repository

import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.naltynbekkz.core.FirebaseQueryLiveData
import com.naltynbekkz.courses.model.LocalFile
import com.naltynbekkz.courses.model.Resource
import com.naltynbekkz.timetable.model.UserCourse
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ResourceRepository @Inject constructor(
    val resourcesDao: com.naltynbekkz.courses.database.ResourcesDao,
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
        success: () -> Boolean,
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
        success: () -> Boolean,
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