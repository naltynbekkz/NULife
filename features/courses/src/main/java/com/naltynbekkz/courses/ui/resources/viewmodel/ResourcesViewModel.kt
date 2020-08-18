package com.naltynbekkz.courses.ui.resources.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naltynbekkz.core.Constants
import com.naltynbekkz.courses.model.Resource
import com.naltynbekkz.courses.repository.ResourceRepository
import com.naltynbekkz.timetable.model.UserCourse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResourcesViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val resourceRepository: ResourceRepository
) : ViewModel() {

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