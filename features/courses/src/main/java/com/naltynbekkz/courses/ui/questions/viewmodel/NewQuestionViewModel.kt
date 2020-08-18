package com.naltynbekkz.courses.ui.questions.viewmodel

import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.naltynbekkz.core.Constants
import com.naltynbekkz.core.Student
import com.naltynbekkz.core.repository.NotificationsRepository
import com.naltynbekkz.core.repository.UserRepository
import com.naltynbekkz.courses.model.Question
import com.naltynbekkz.courses.repository.QuestionsRepository
import com.naltynbekkz.timetable.model.UserCourse

class NewQuestionViewModel @ViewModelInject constructor(
    @Assisted savedStateHandle: SavedStateHandle,
    private val questionsRepository: QuestionsRepository,
    private val notificationsRepository: NotificationsRepository,
    userRepository: UserRepository
) : ViewModel() {

    val question: Question = if (savedStateHandle.get<UserCourse>(Constants.USER_COURSE) != null) {
        Question(userCourse = savedStateHandle[Constants.USER_COURSE]!!)
    } else {
        savedStateHandle[Constants.QUESTION]!!
    }

    val user = userRepository.user
    val topics = questionsRepository.getTopics(question)

    fun post(
        question: Question,
        anonymous: Boolean,
        allSections: Boolean,
        success: () -> Boolean,
        failure: () -> Unit,
        images: ArrayList<Uri>,
        done: (Int) -> Unit
    ) {
        if (allSections) {
            question.sectionId = 0L
        }

        question.author = Student(
            user.value!!, anonymous
        )
        question.timestamp = System.currentTimeMillis() / 1000
        questionsRepository.post(
            question,
            fun(id) {
                notificationsRepository.follow(id, "question", success)
            },
            failure, images, done
        )
    }

    fun editQuestion(
        question: Question,
        anonymous: Boolean,
        success: () -> Boolean,
        failure: () -> Unit
    ) {
        question.author = Student(
            user.value!!, anonymous
        )
        question.timestamp = System.currentTimeMillis() / 1000
        questionsRepository.editQuestion(question, success, failure)
    }

}