package com.naltynbekkz.nulife.ui.courses.questions.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.BottomSheetFollowBinding

class FollowBottomSheet(
    private val id: String,
    private val following: Boolean,
    private val follow: (String, Boolean) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetFollowBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_follow, container, false)

        binding.following = following
        binding.followButton.setOnClickListener {
            follow(id, binding.following!!)
            dismiss()
        }

        return binding.root
    }
}