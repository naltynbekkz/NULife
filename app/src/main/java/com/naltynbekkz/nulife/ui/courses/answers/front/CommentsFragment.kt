package com.naltynbekkz.nulife.ui.courses.answers.front

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentCommentsBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Comment
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.courses.answers.adapter.CommentsAdapter
import com.naltynbekkz.nulife.ui.courses.answers.viewmodel.CommentsViewModel
import com.naltynbekkz.nulife.util.EditDeleteBottomSheet
import javax.inject.Inject

class CommentsFragment : Fragment() {

    // TODO: set anonymous when editing the comment

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val args: CommentsFragmentArgs by navArgs()
    private val viewModel: CommentsViewModel by viewModels {
        viewModelProvider.create(this, args.toBundle())
    }

    private lateinit var binding: FragmentCommentsBinding
    private lateinit var commentsAdapter: CommentsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).coursesComponent.inject(this)
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).showBottomNavigation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false)
        (activity as MainActivity).hideBottomNavigation()
        setHasOptionsMenu(true)

        binding.comment = Comment()
        binding.user = FirebaseAuth.getInstance().currentUser
        binding.anonymous = false

        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        val user = viewModel.user.value!!

        viewModel.answer.observe(viewLifecycleOwner, Observer { answer ->
            binding.answer = answer

            val myVote = answer.getVote(user.uid)
            binding.myVote = myVote
            binding.itemAnswer.upvoteLayout.setOnClickListener {
                viewModel.vote(if (myVote != null && myVote) null else true)
            }
            binding.itemAnswer.downvoteLayout.setOnClickListener {
                viewModel.vote(if (myVote != null && !myVote) null else false)
            }
            commentsAdapter.submitList(answer.comments)
        })

        commentsAdapter = CommentsAdapter {
            if (it.author.id == user.uid || it.author.id == user.anonymousId) {
                EditDeleteBottomSheet(
                    edit = fun() {
                        binding.comment = it
                    },
                    delete = fun() {
                        viewModel.deleteComment(it)
                    }
                ).show(parentFragmentManager, "tag")
                true
            } else {
                false
            }
        }
        binding.commentsRecyclerView.adapter = commentsAdapter

        binding.cancel.setOnClickListener {
            binding.comment = Comment()
        }
        binding.send.setOnClickListener {
            if (binding.commentEditText.text.isNotEmpty()) {
                freeze()

                viewModel.comment(
                    binding.comment!!.apply {
                        timestamp = System.currentTimeMillis() / 1000
                        body = binding.commentEditText.text.toString()
                    },
                    binding.anonymous!!,
                    success = fun() {
                        unfreeze()
                        binding.comment = Comment()
                    },
                    failure = fun() {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
        }

        return binding.root
    }


    private fun unfreeze() {
        binding.commentEditText.isEnabled = true
        binding.send.isEnabled = true
    }

    private fun freeze() {
        binding.commentEditText.isEnabled = false
        binding.send.isEnabled = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.anonymous -> {
                if (binding.anonymous!!) {
                    item.icon = resources.getDrawable(R.drawable.ic_profile, requireContext().theme)
                    item.title = resources.getString(R.string.publick)
                    binding.anonymous = false
                } else {
                    item.icon =
                        resources.getDrawable(R.drawable.ic_anonymous, requireContext().theme)
                    item.title = resources.getString(R.string.anonymous)
                    binding.anonymous = true
                }
            }
        }
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.comments_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showBottomNavigation()
    }

}
