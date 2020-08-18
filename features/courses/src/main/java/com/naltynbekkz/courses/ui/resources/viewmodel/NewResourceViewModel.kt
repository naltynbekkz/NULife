package com.naltynbekkz.courses.ui.resources.viewmodel

import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.core.Student
import com.naltynbekkz.core.repository.UserRepository
import com.naltynbekkz.core.Constants
import com.naltynbekkz.courses.model.LocalFile
import com.naltynbekkz.courses.model.Resource
import com.naltynbekkz.courses.repository.ResourceRepository
import com.naltynbekkz.timetable.model.UserCourse

class NewResourceViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val resourceRepository: ResourceRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val userCourse: UserCourse = savedStateHandle[Constants.USER_COURSE]!!

    fun post(
        resource: Resource,
        files: ArrayList<LocalFile>,
        success: () -> Boolean,
        failure: () -> Unit,
        done: (Int) -> Unit
    ) {
        resource.author = Student(
            userRepository.user.value!!, false
        )
        resourceRepository.post(resource, userCourse, files, success, failure, done)
    }

    fun post(
        resource: Resource,
        success: () -> Boolean,
        failure: () -> Unit,
        images: ArrayList<Uri>,
        compress: (Uri) -> ByteArray,
        done: (Int) -> Unit
    ) {
        resource.author = Student(
            userRepository.user.value!!, false
        )
        resourceRepository.post(resource, userCourse, success, failure, images, compress, done)

    }

}