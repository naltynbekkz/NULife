package com.naltynbekkz.courses.ui.courses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.courses.databinding.ItemCourseBinding
import com.naltynbekkz.timetable.model.UserCourse

class UserCoursesAdapter(
    private val click: (UserCourse) -> Unit,
    private val longClick: (UserCourse) -> Boolean
) : ListAdapter<UserCourse, UserCoursesAdapter.UserCourseViewHolder>(UserCourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserCourseViewHolder(
            ItemCourseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: UserCourseViewHolder, position: Int) =
        holder.bind(getItem(position), click, longClick)

    class UserCourseViewHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            userCourse: UserCourse,
            click: (UserCourse) -> Unit,
            longClick: (UserCourse) -> Boolean
        ) {
            binding.userCourse = userCourse
            binding.root.setOnLongClickListener {
                longClick(userCourse)
            }
            binding.more.setOnClickListener {
                longClick(userCourse)
            }

            binding.root.setOnClickListener {
                click(userCourse)
            }
        }
    }

}

private class UserCourseDiffCallback : DiffUtil.ItemCallback<UserCourse>() {

    override fun areItemsTheSame(oldItem: UserCourse, newItem: UserCourse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserCourse, newItem: UserCourse): Boolean {
        return oldItem == newItem
    }
}