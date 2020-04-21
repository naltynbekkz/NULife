package com.naltynbekkz.nulife.ui.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.ItemMarketBinding
import com.naltynbekkz.nulife.model.Item

class MarketAdapter(
    private val click: (Item) -> Unit,
    private val like: (Item) -> Unit,
    private val dislike: (String) -> Unit
) : RecyclerView.Adapter<MarketAdapter.ItemViewHolder>() {

    private var items: ArrayList<Item> = ArrayList()
    private var savedItems: HashMap<String, Item> = HashMap()

    fun setData(items: List<Item>) {
        this.items = ArrayList<Item>().apply {
            items.forEach {
                add(it)
            }
        }
        notifyDataSetChanged()
    }

    fun setSavedData(favoriteItems: List<Item>) {
        this.savedItems.clear()
        favoriteItems.forEach {
            this.savedItems[it.id] = it
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ItemMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(
            items[position],
            click,
            like,
            dislike,
            savedItems.contains(items[position].id)
        )

    override fun getItemCount(): Int = items.size

    class ItemViewHolder(val binding: ItemMarketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Item,
            click: (Item) -> Unit,
            like: (Item) -> Unit,
            dislike: (String) -> Unit,
            favorite: Boolean
        ) {
            binding.item = item
            binding.saved = favorite
            binding.root.setOnClickListener {
                click(item)
            }

            binding.likeLayout.setOnClickListener {
                if (favorite) {
                    dislike(item.id)
                } else {
                    like(item)
                }
            }

        }
    }

}
