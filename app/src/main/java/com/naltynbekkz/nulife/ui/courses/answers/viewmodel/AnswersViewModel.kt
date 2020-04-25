package com.naltynbekkz.nulife.ui.courses.answers.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.Answer
import com.naltynbekkz.nulife.model.Question
import com.naltynbekkz.nulife.repository.AnswersRepository
import com.naltynbekkz.nulife.repository.QuestionsRepository
import com.naltynbekkz.nulife.repository.UserRepository
import com.naltynbekkz.nulife.util.Constants
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject


class AnswersViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    questionsRepository: QuestionsRepository,
    private val answersRepository: AnswersRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val _question: Question = savedStateHandle[Constants.QUESTION]!!

    @AssistedInject.Factory
    interface Factory : ViewModelAssistedFactory<AnswersViewModel>

    val user = userRepository.user
    val answers = answersRepository.getAnswers(_question)

    val question = questionsRepository.getQuestion(_question)


    fun delete(answer: Answer) {
        answersRepository.delete(answer)
    }

    fun vote(answer: Answer, vote: Boolean?) {
        answersRepository.vote(answer, vote)
    }

}