package com.naltynbekkz.nulife.ui.market.front

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.ItemRequestBinding
import com.naltynbekkz.nulife.model.Request

class RequestsAdapter(
    private var requests: ArrayList<Request>
) : RecyclerView.Adapter<RequestsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ContactViewHolder(
        ItemRequestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(requests[position])
    }

    override fun getItemCount() = requests.size

    inner class ContactViewHolder(private val binding: ItemRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            request: Request
        ) {
            binding.expanded = false
            binding.request = request
            binding.root.setOnClickListener {
                binding.expanded = !binding.expanded!!
            }
        }
    }
}