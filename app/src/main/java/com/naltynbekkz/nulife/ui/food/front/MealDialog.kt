package com.naltynbekkz.nulife.ui.food.front

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.DialogMealBinding
import com.naltynbekkz.nulife.model.Cafe
import com.naltynbekkz.nulife.model.Meal

class MealDialog(private val meal: Meal) :
    AppCompatDialogFragment() {


    private lateinit var binding: DialogMealBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_meal,
            null,
            false
        )

        binding.meal = meal

        return AlertDialog.Builder(activity).setView(binding.root).create()
    }
}