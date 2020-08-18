package com.naltynbekkz.courses.ui.questions.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.naltynbekkz.core.Constants
import com.naltynbekkz.core.repository.NotificationsRepository
import com.naltynbekkz.core.repository.UserRepository
import com.naltynbekkz.courses.model.Question
import com.naltynbekkz.courses.repository.QuestionsRepository
import com.naltynbekkz.timetable.model.UserCourse
import com.zhuinden.livedatacombinetuplekt.combineTuple

class QuestionsViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val questionsRepository: QuestionsRepository,
    private val notificationsRepository: NotificationsRepository,
    val userRepository: UserRepository
) : ViewModel() {

    val userCourse: UserCourse = savedStateHandle[Constants.USER_COURSE]!!

    val user = userRepository.user

    val questions = Transformations.map(
        combineTuple(
            questionsRepository.getAllQuestions(userCourse),
            questionsRepository.getSectionQuestions(userCourse),
            notificationsRepository.data
        )
    ) {
        getQuestions(it.first, it.second, it.third)
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


    private fun getQuestions(
        allQuestions: ArrayList<Question>?,
        sectionQuestions: ArrayList<Question>?,
        following: HashSet<String>?
    ): ArrayList<Pair<String, ArrayList<Question>>> {
        following?.apply {
            listOf(allQuestions, sectionQuestions).forEach { questions ->
                questions?.forEach {
                    if (contains(it.id)) {
                        it.following = true
                    }
                }
            }
        }

        val questions = ArrayList<Question>().apply {
            addAll(
                ArrayList<Question>().apply {
                    allQuestions?.let {
                        addAll(it)
                    }
                    sectionQuestions?.let {
                        addAll(it)
                    }
                }.sortedWith(compareBy { it.timestamp })
            )
        }

        questions.reverse()

        val topics = ArrayList<Pair<String, ArrayList<Question>>>()
        questions.forEach { question ->
            var contains = false
            for (topic in topics) {
                if (topic.first == question.topic) {
                    topic.second.add(question)
                    contains = true
                    break
                }
            }
            if (!contains) {
                topics.add(Pair(question.topic, ArrayList<Question>().apply {
                    add(question)
                }))
            }
        }

        return topics
    }


}