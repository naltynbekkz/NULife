package com.naltynbekkz.courses.ui.answers.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.core.Student
import com.naltynbekkz.core.repository.UserRepository
import com.naltynbekkz.core.Constants
import com.naltynbekkz.courses.model.Answer
import com.naltynbekkz.courses.model.Comment
import com.naltynbekkz.courses.repository.AnswersRepository


class CommentsViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val answersRepository: AnswersRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val _answer: Answer = savedStateHandle[Constants.ANSWER]!!

    val user = userRepository.user
    val answer = answersRepository.getAnswer(_answer)

    fun vote(vote: Boolean?) {
        answersRepository.vote(answer.value!!, vote)
    }

    fun comment(
        comment: Comment,
        anonymous: Boolean,
        success: () -> Unit,
        failure: () -> Unit
    ) {
        comment.author = Student(
            user.value!!, anonymous
        )
        answersRepository.comment(answer.value!!, comment, success, failure)
    }

    fun deleteComment(comment: Comment) {
        answersRepository.deleteComment(answer.value!!, comment)
    }

}