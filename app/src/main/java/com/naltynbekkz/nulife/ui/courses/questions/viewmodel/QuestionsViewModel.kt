package com.naltynbekkz.nulife.ui.courses.questions.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.Question
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.repository.NotificationsRepository
import com.naltynbekkz.nulife.repository.QuestionsRepository
import com.naltynbekkz.nulife.repository.UserRepository
import com.naltynbekkz.nulife.util.Constants
import com.naltynbekkz.nulife.util.Convert
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.zhuinden.livedatacombinetuplekt.combineTuple

class QuestionsViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val questionsRepository: QuestionsRepository,
    private val notificationsRepository: NotificationsRepository,
    val userRepository: UserRepository
) : ViewModel() {

    val userCourse: UserCourse = savedStateHandle[Constants.USER_COURSE]!!

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<QuestionsViewModel>

    val user = userRepository.user

    val questions = Transformations.map(
        combineTuple(
            questionsRepository.getAllQuestions(userCourse),
            questionsRepository.getSectionQuestions(userCourse),
            notificationsRepository.data
        )
    ) {
        Convert.getQuestions(it.first, it.second, it.third)
    }

    fun delete(question: Question) {
        questionsRepository.delete(question)
    }


    fun follow(id: String, exists: Boolean) {
        if (exists) {
            notificationsRepository.unFollow(id)
        } else {
            notificationsRepository.follow(id, "question")
        }
    }


}