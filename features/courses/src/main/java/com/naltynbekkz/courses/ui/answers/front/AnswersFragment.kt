package com.naltynbekkz.courses.ui.answers.front

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.naltynbekkz.core.EditDeleteBottomSheet
import com.naltynbekkz.core.User
import com.naltynbekkz.courses.R
import com.naltynbekkz.courses.databinding.FragmentAnswersBinding
import com.naltynbekkz.courses.model.Answer
import com.naltynbekkz.courses.ui.answers.adapter.AnswersAdapter
import com.naltynbekkz.courses.ui.answers.viewmodel.AnswersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnswersFragment : Fragment() {

    private val viewModel: AnswersViewModel by viewModels()

    private lateinit var binding: FragmentAnswersBinding
    private lateinit var adapter: AnswersAdapter

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = AnswersAdapter(
            user = viewModel.user.value!!,
            click = fun(answer: Answer) {
                findNavController().navigate(
                    AnswersFragmentDirections.actionAnswersFragmentToCommentsFragment(answer)
                )
            },
            longClick = fun(answer: Answer): Boolean {
                if (user != null && answer.author.id == user!!.uid || answer.author.id == user!!.anonymousId) {
                    EditDeleteBottomSheet(
                        edit = fun() {
                            findNavController().navigate(
                                AnswersFragmentDirections.actionAnswersFragmentToNewAnswerFragment(
                                    null,
                                    answer
                                )
                            )
                        },
                        delete = fun() {
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Are you sure?")
                                .setPositiveButton("Delete") { _, _ ->
                                    viewModel.delete(answer)
                                }
                                .setNegativeButton("Cancel") { _, _ ->

                                }.show()
                        }
                    ).show(parentFragmentManager, "AnswersActivity")
                    return true
                } else {
                    return false
                }
            },
            vote = viewModel::vote
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.answers_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.answer -> {
                findNavController().navigate(
                    AnswersFragmentDirections.actionAnswersFragmentToNewAnswerFragment(
                        viewModel.question.value!!,
                        null
                    )
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_answers, container, false)
        setHasOptionsMenu(true)

        viewModel.question.observe(viewLifecycleOwner, Observer { question ->
            binding.question = question
            // TODO: cheto tut neto
            binding.toolbar.title = question.topic
        })

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            this.user = it
        })



        binding.answersRecyclerView.adapter = adapter
        viewModel.answers.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        return binding.root
    }

}