package com.naltynbekkz.nulife.ui.courses.courses.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.SuggestedCourse
import com.naltynbekkz.nulife.model.UserCourse
import kotlinx.android.synthetic.main.item_enroll.view.*


class EnrollAdapter(
    private val click: (String) -> Unit
) :
    ListAdapter<SuggestedCourse, EnrollAdapter.EnrollViewHolder>(SuggestedCourseDiffCallback()),
    Filterable {

    private var enrolledCourses: ArrayList<UserCourse> = ArrayList()

    private var allCourses: ArrayList<SuggestedCourse> = ArrayList()

    private var unEnrolledCourses: ArrayList<SuggestedCourse> = ArrayList()

    fun setEnrolledData(enrolledCourses: ArrayList<UserCourse>) {
        this.enrolledCourses = enrolledCourses
        validate()
    }

    fun setData(courses: ArrayList<SuggestedCourse>) {
        this.allCourses = courses
        validate()
    }

    private fun validate() {
        unEnrolledCourses = ArrayList<SuggestedCourse>().apply {
            allCourses.forEach {
                add(it)
            }
        }
        val toBeDeleted = ArrayList<SuggestedCourse>()
        unEnrolledCourses.forEach { course ->
            enrolledCourses.forEach { enrolledCourse ->
                if (course.id == enrolledCourse.id) {
                    toBeDeleted.add(course)
                }
            }
        }
        toBeDeleted.forEach {
            unEnrolledCourses.remove(it)
        }
        filter.filter("")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EnrollViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_enroll, parent, false)
        )

    override fun onBindViewHolder(holder: EnrollViewHolder, position: Int) =
        holder.bind(getItem(position), click)

    class EnrollViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            suggestedCourse: SuggestedCourse,
            click: (String) -> Unit
        ) = with(itemView) {
            title.text = suggestedCourse.id
            name.text = suggestedCourse.name
            setOnClickListener {
                click(suggestedCourse.id)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val string = charSequence.toString()

                return FilterResults().apply {
                    values = ArrayList<SuggestedCourse>().apply {
                        unEnrolledCourses.forEach {
                            if (string.isEmpty() ||
                                it.id.toLowerCase().contains(string.toLowerCase()) ||
                                it.name.toLowerCase().contains(string.toLowerCase())
                            ) {
                                add(it)
                            }
                        }
                    }
                }
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                submitList(filterResults.values as ArrayList<SuggestedCourse>)
            }
        }
    }

}

private class SuggestedCourseDiffCallback : DiffUtil.ItemCallback<SuggestedCourse>() {

    override fun areItemsTheSame(oldItem: SuggestedCourse, newItem: SuggestedCourse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SuggestedCourse, newItem: SuggestedCourse): Boolean {
        return oldItem == newItem
    }
}