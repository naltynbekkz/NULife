package com.naltynbekkz.nulife.ui.courses.resources.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.Resource
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.repository.ResourceRepository
import com.naltynbekkz.nulife.util.Constants
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResourcesViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val resourceRepository: ResourceRepository
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<ResourcesViewModel>

    private val userCourse: UserCourse = savedStateHandle[Constants.USER_COURSE]!!

    val resources = resourceRepository.getResources(userCourse)

    val savedResources = resourceRepository.savedResources

    fun insert(resource: Resource) {
        viewModelScope.launch(Dispatchers.IO) {
            resourceRepository.insert(resource)
        }
    }

    fun delete(resource: Resource) {
        viewModelScope.launch(Dispatchers.IO) {
            resourceRepository.delete(resource)
        }
    }

    fun update(resource: Resource) {
        viewModelScope.launch(Dispatchers.IO) {
            resourceRepository.update(resource)
        }
    }


}