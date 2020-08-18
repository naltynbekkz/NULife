package com.naltynbekkz.courses.ui.questions.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.courses.databinding.ItemSelectTopicBinding

class SelectTopicAdapter(
    private val topics: ArrayList<String>,
    private val setTopic: (String) -> Unit
) : RecyclerView.Adapter<SelectTopicAdapter.ItemViewHolder>() {

    private var currentTopic: String = ""

    fun setCurrentTopic(currentTopic: String) {
        this.currentTopic = currentTopic
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemSelectTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return topics.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(topics[position], setTopic, currentTopic)
    }

    class ItemViewHolder(val binding: ItemSelectTopicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            topic: String,
            setTopic: (String) -> Unit,
            currentTopic: String
        ) = with(itemView) {
            binding.name.text = topic
            if (currentTopic == topic) {
                binding.icon.visibility = View.VISIBLE
                setTopic(topic)
            }
            setOnClickListener {
                setTopic(topic)
            }
        }
    }

}
