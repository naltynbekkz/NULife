package com.naltynbekkz.food.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.food.databinding.ItemMealBinding
import com.naltynbekkz.food.model.Meal


class MealsAdapter(
    private val active: Boolean,
    private val click: (Meal) -> Unit
) : ListAdapter<Meal, MealsAdapter.MealViewHolder>(
    MealDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(
            ItemMealBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(getItem(position), click, active)
    }


    class MealViewHolder(val binding: ItemMealBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            meal: Meal,
            click: (Meal) -> Unit,
            active: Boolean
        ) {
            binding.meal = meal
            binding.active = active
            binding.root.setOnClickListener {
                click(meal)
            }
        }
    }

}


private class MealDiffCallback : DiffUtil.ItemCallback<Meal>() {

    override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
        return oldItem == newItem
    }
}