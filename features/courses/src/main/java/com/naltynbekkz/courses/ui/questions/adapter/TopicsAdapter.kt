package com.naltynbekkz.courses.ui.questions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.courses.databinding.ItemTopicBinding
import com.naltynbekkz.courses.model.Question

class TopicsAdapter(
    private val click: (Question) -> Unit,
    private val longClick: (Question) -> Boolean
) : ListAdapter<Pair<String, ArrayList<Question>>, TopicsAdapter.ItemViewHolder>(TopicsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            ItemTopicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(getItem(position), click, longClick)

    class ItemViewHolder(val binding: ItemTopicBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Pair<String, ArrayList<Question>>,
            click: (Question) -> Unit,
            longClick: (Question) -> Boolean
        ) {
            binding.expanded = false
            binding.topic = item.first
            binding.questionCount = item.second.size
            binding.recyclerView.adapter = QuestionsAdapter(
                click,
                longClick
            ).apply {
                submitList(item.second)
            }
            binding.root.setOnClickListener {
                binding.expanded = !binding.expanded!!
            }

        }
    }

}


private class TopicsDiffCallback : DiffUtil.ItemCallback<Pair<String, ArrayList<Question>>>() {

    override fun areItemsTheSame(
        oldItem: Pair<String, ArrayList<Question>>,
        newItem: Pair<String, ArrayList<Question>>
    ): Boolean {
        return oldItem.first == newItem.first
    }

    override fun areContentsTheSame(
        oldItem: Pair<String, ArrayList<Question>>,
        newItem: Pair<String, ArrayList<Question>>
    ): Boolean {
        return oldItem.first == newItem.first && oldItem.second == newItem.second
    }
}