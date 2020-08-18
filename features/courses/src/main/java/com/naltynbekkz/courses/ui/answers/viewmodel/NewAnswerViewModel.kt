package com.naltynbekkz.courses.ui.answers.viewmodel

import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.core.Student
import com.naltynbekkz.core.repository.UserRepository
import com.naltynbekkz.core.Constants
import com.naltynbekkz.courses.model.Answer
import com.naltynbekkz.courses.model.Question
import com.naltynbekkz.courses.repository.AnswersRepository

class NewAnswerViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val answersRepository: AnswersRepository,
    userRepository: UserRepository
) : ViewModel() {

    val answer: Answer = if (savedStateHandle.get<Question>(Constants.QUESTION) != null) {
        Answer(question = savedStateHandle[Constants.QUESTION]!!)
    } else {
        savedStateHandle[Constants.ANSWER]!!
    }

    val user = userRepository.user

    fun answer(
        anonymous: Boolean,
        failure: () -> Unit,
        images: ArrayList<Uri>,
        success: () -> Boolean,
        done: (Int) -> Unit
    ) {
        answer.apply {
            author = Student(
                user.value!!,
                anonymous
            )
            timestamp = System.currentTimeMillis() / 1000
        }

        answersRepository.post(answer, failure, images, success, done)
    }

    fun editAnswer(
        anonymous: Boolean,
        success: () -> Boolean,
        failure: () -> Unit
    ) {
        answer.apply {
            author = Student(
                user.value!!,
                anonymous
            )
            timestamp = System.currentTimeMillis() / 1000
        }
        answersRepository.editAnswer(answer, success, failure)
    }

}