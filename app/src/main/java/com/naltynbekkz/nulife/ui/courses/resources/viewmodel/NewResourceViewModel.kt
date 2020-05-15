package com.naltynbekkz.nulife.ui.courses.resources.viewmodel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.LocalFile
import com.naltynbekkz.nulife.model.Resource
import com.naltynbekkz.nulife.model.Student
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.repository.ResourceRepository
import com.naltynbekkz.nulife.repository.UserRepository
import com.naltynbekkz.nulife.util.Constants
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class NewResourceViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val resourceRepository: ResourceRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory: ViewModelAssistedFactory<NewResourceViewModel>

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