package com.naltynbekkz.courses.ui.answers.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.core.repository.UserRepository
import com.naltynbekkz.core.Constants
import com.naltynbekkz.courses.model.Answer
import com.naltynbekkz.courses.model.Question
import com.naltynbekkz.courses.repository.AnswersRepository
import com.naltynbekkz.courses.repository.QuestionsRepository


class AnswersViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    questionsRepository: QuestionsRepository,
    private val answersRepository: AnswersRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val _question: Question = savedStateHandle[Constants.QUESTION]!!

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