package com.naltynbekkz.courses.ui.questions.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.naltynbekkz.core.EditDeleteBottomSheet
import com.naltynbekkz.core.User
import com.naltynbekkz.courses.databinding.FragmentQuestionsBinding
import com.naltynbekkz.courses.model.Question
import com.naltynbekkz.courses.ui.courses.front.CourseFragmentDirections
import com.naltynbekkz.courses.ui.questions.adapter.TopicsAdapter
import com.naltynbekkz.courses.ui.questions.viewmodel.QuestionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionsFragment : Fragment() {

    private val viewModel: QuestionsViewModel by viewModels()

    private var user: User? = null

    private lateinit var topicsAdapter: TopicsAdapter

    private lateinit var binding: FragmentQuestionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuestionsBinding.inflate(inflater, container, false)


        viewModel.user.observe(viewLifecycleOwner, Observer {
            this.user = it
        })

        viewModel.questions.observe(viewLifecycleOwner, Observer {
            topicsAdapter.submitList(it)
        })

        binding.recyclerView.adapter = topicsAdapter

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        topicsAdapter = TopicsAdapter(
            click = fun(question: Question) {
                findNavController().navigate(
                    CourseFragmentDirections.actionCourseFragmentToAnswersFragment(question)
                )
            },
            longClick = fun(question): Boolean {
                user?.let {
                    if (question.author.id == it.uid || question.author.id == it.anonymousId) {
                        EditDeleteBottomSheet(
                            edit = fun() {
                                findNavController().navigate(
                                    CourseFragmentDirections.actionCourseFragmentToNewQuestionFragment(
                                        question,
                                        null
                                    )
                                )
                            },
                            delete = fun() {
                                MaterialAlertDialogBuilder(requireContext())
                                    .setTitle("Are you sure?")
                                    .setPositiveButton("Delete") { _, _ ->
                                        viewModel.delete(question)
                                    }
                                    .setNegativeButton("Cancel") { _, _ ->

                                    }.show()
                            }
                        ).show(parentFragmentManager, "tag")
                    } else {
                        FollowBottomSheet(
                            question.id,
                            question.following,
                            viewModel::follow
                        ).show(
                            parentFragmentManager,
                            "tag"
                        )
                    }
                }
                return true
            }
        )

    }

}
