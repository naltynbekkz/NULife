package com.naltynbekkz.nulife.ui.courses.questions.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.R
import kotlinx.android.synthetic.main.item_select_topic.view.*

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
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_select_topic,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return topics.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(topics[position], setTopic, currentTopic)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            topic: String,
            setTopic: (String) -> Unit,
            currentTopic: String
        ) = with(itemView) {
            name.text = topic
            if (currentTopic == topic) {
                icon.visibility = View.VISIBLE
                setTopic(topic)
            }
            setOnClickListener {
                setTopic(topic)
            }
        }
    }

}
