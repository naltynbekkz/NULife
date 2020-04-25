package com.naltynbekkz.nulife.ui.courses.answers.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.nulife.di.ViewModelAssistedFactory
import com.naltynbekkz.nulife.model.Answer
import com.naltynbekkz.nulife.model.Comment
import com.naltynbekkz.nulife.model.Student
import com.naltynbekkz.nulife.repository.AnswersRepository
import com.naltynbekkz.nulife.repository.UserRepository
import com.naltynbekkz.nulife.util.Constants
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject


class CommentsViewModel @AssistedInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val answersRepository: AnswersRepository,
    userRepository: UserRepository
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory: ViewModelAssistedFactory<CommentsViewModel>

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