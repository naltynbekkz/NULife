package com.naltynbekkz.nulife.ui.courses.answers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.ItemAnswerBinding
import com.naltynbekkz.nulife.model.Answer
import com.naltynbekkz.nulife.model.User

class AnswersAdapter(
    private val user: User,
    private val click: (Answer) -> Unit,
    private val longClick: (Answer) -> Boolean,
    private val vote: (Answer, Boolean?) -> Unit
) : ListAdapter<Answer, AnswersAdapter.AnswerViewHolder>(AnswerDiffCallback()) {

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) =
        holder.bind(getItem(position), user, click, longClick, vote)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AnswerViewHolder(
            ItemAnswerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    class AnswerViewHolder(val binding: ItemAnswerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            answer: Answer,
            user: User,
            click: (Answer) -> Unit,
            longClick: (Answer) -> Boolean,
            vote: (Answer, Boolean?) -> Unit
        ) {
            binding.answer = answer
            binding.myVote = answer.getVote(user.uid)
            binding.root.setOnClickListener {
                click(answer)
            }
            binding.root.setOnLongClickListener {
                longClick(answer)
            }
            binding.upvoteLayout.setOnClickListener {
                if (binding.answer!!.getVote(user.uid) == true) {
                    vote(binding.answer!!, null)
                } else {
                    vote(binding.answer!!, true)
                }
            }
            binding.downvoteLayout.setOnClickListener {
                if (binding.answer!!.getVote(user.uid) == false) {
                    vote(binding.answer!!, null)
                } else {
                    vote(binding.answer!!, false)
                }
            }
        }
    }

}


private class AnswerDiffCallback : DiffUtil.ItemCallback<Answer>() {

    override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean {
        return oldItem == newItem
    }
}