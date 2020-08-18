package com.naltynbekkz.courses.ui.questions.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.courses.databinding.BottomSheetFollowBinding

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
        binding = BottomSheetFollowBinding.inflate(inflater, container, false)

        binding.following = following
        binding.followButton.setOnClickListener {
            follow(id, binding.following!!)
            dismiss()
        }

        return binding.root
    }
}