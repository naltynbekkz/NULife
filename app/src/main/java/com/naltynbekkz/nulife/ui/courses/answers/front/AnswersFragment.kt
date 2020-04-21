package com.naltynbekkz.nulife.ui.courses.answers.front

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentAnswersBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Answer
import com.naltynbekkz.nulife.model.User
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.courses.answers.adapter.AnswersAdapter
import com.naltynbekkz.nulife.ui.courses.answers.viewmodel.AnswersViewModel
import com.naltynbekkz.nulife.util.EditDeleteBottomSheet
import javax.inject.Inject

class AnswersFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val args: AnswersFragmentArgs by navArgs()
    private val viewModel: AnswersViewModel by viewModels {
        viewModelProvider.create(this, args.toBundle())
    }

    private lateinit var binding: FragmentAnswersBinding
    private lateinit var adapter: AnswersAdapter

    private var user: User? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).coursesComponent.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.answers_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.answer -> {
                findNavController().navigate(
                    AnswersFragmentDirections.actionAnswersFragmentToNewAnswerFragment(viewModel.question.value!!)
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
                onBackPressed()
            }
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            this.user = it
        })

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
                                AnswersFragmentDirections.actionAnswersFragmentToEditAnswerFragment(answer)
                            )
                        },
                        delete = fun() {
                            MaterialAlertDialogBuilder(context)
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

        binding.answersRecyclerView.adapter = adapter
        viewModel.answers.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        return binding.root
    }

}