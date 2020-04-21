package com.naltynbekkz.nulife.ui.market.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.BottomSheetMarketFilterBinding


class FilterBottomSheet(
    private val filter: Filter,
    private val click: (Filter) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetMarketFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_market_filter, container, false)

        // categories
        resources.getStringArray(R.array.categories).forEach {
            val chip = layoutInflater.inflate(
                R.layout.item_chip_category,
                binding.categories,
                false
            ) as Chip
            chip.text = it
            if (it == filter.category) {
                chip.isChecked = true
            }
            binding.categories.addView(chip)
        }

        binding.categories.children.forEach {
            if ((it as Chip).text == filter.category) {
                it.isChecked = true
            }
        }
        // female
        binding.female.isChecked = filter.female
        // sell
        when (filter.sell) {
            null -> (binding.all as MaterialButton).isChecked = true
            true -> (binding.selling as MaterialButton).isChecked = true
            false -> (binding.buying as MaterialButton).isChecked = true
        }
        // sort
        binding.sort.setAdapter(
            ArrayAdapter(
                context!!,
                R.layout.item_spinner_layout,
                resources.getStringArray(R.array.sort)
            )
        )
        binding.sort.setText(binding.sort.adapter.getItem(filter.sort).toString(), false)
        binding.sort.setOnItemClickListener { _, _, position, _ ->
            filter.sort = position
        }
        // apply
        binding.apply.setOnClickListener {
            var category: String? = null
            binding.categories.children.forEach {
                if ((it as Chip).isChecked) {
                    category = it.text.toString()
                }
            }
            filter.category = category
            filter.female = binding.female.isChecked
            when {
                (binding.all as MaterialButton).isChecked -> filter.sell = null
                (binding.selling as MaterialButton).isChecked -> filter.sell = true
                (binding.buying as MaterialButton).isChecked -> filter.sell = false
            }
            click(filter)
            dismiss()
        }

        return binding.root

    }

}