package com.naltynbekkz.food.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.food.databinding.FooterSmallReviewBinding
import com.naltynbekkz.food.databinding.ItemSmallReviewBinding
import com.naltynbekkz.food.model.Review

class SmallReviewAdapter(
    private val seeAll: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var reviews: ArrayList<Review> = ArrayList()

    companion object {
        const val ITEM = 0
        const val FOOTER = 1
    }

    fun setData(reviews: ArrayList<Review>) {
        this.reviews = reviews
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM) {
            return ItemViewHolder(
                ItemSmallReviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return FooterViewHolder(
                FooterSmallReviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int) = if (position < 3) ITEM else FOOTER

    override fun getItemCount(): Int {
        return if (reviews.size < 4) reviews.size else 4
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(reviews[position], seeAll)
        } else if (holder is FooterViewHolder) {
            holder.bind()
        }
    }


    class ItemViewHolder(val binding: ItemSmallReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review, seeAll: () -> Unit) {
            binding.review = review
            binding.root.setOnClickListener {
                seeAll()
            }
        }
    }

    inner class FooterViewHolder(val binding: FooterSmallReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
                seeAll()
            }
        }
    }

}