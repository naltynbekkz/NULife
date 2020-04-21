package com.naltynbekkz.nulife.ui.courses.answers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.ItemCommentBinding
import com.naltynbekkz.nulife.model.Comment

class CommentsAdapter(
    private val longClick: (Comment) -> Boolean
) : ListAdapter<Comment, CommentsAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CommentViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) =
        holder.bind(getItem(position), longClick)

    class CommentViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            comment: Comment,
            longClick: (Comment) -> Boolean
        ) {
            binding.comment = comment
            binding.root.setOnLongClickListener {
                longClick(comment)
            }

        }
    }
}

private class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {

    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}