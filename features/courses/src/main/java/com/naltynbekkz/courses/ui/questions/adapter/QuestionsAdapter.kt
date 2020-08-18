package com.naltynbekkz.courses.ui.questions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.courses.databinding.ItemQuestionBinding
import com.naltynbekkz.courses.model.Question

class QuestionsAdapter(
    private val click: (Question) -> Unit,
    private val longClick: (Question) -> Boolean
) : ListAdapter<Question, QuestionsAdapter.ItemViewHolder>(QuestionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            ItemQuestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(getItem(position), click, longClick)


    class ItemViewHolder(val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            question: Question,
            click: (Question) -> Unit,
            longClick: (Question) -> Boolean
        ) {
            binding.question = question
            binding.layout.setOnClickListener {
                click(question)
            }
            binding.layout.setOnLongClickListener {
                longClick(question)
            }
        }
    }

}

private class QuestionDiffCallback : DiffUtil.ItemCallback<Question>() {

    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }
}
