package com.naltynbekkz.nulife.ui.timetable.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.BottomSheetAssociateBinding
import com.naltynbekkz.nulife.model.Associate
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.ui.timetable.adapter.AssociatesAdapter
import java.util.*
import kotlin.collections.ArrayList

class AssociateBottomSheet(
    var userCourses: ArrayList<Associate>?,
    var userClubs: ArrayList<Associate>?,
    var routines: List<Occurrence>? = null,
    private val click: (Associate) -> Unit
) : BottomSheetDialogFragment() {


    lateinit var binding: BottomSheetAssociateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_associate, container, false)

        binding.recyclerView.adapter = AssociatesAdapter(
            associates = ArrayList(TreeMap<String, Associate>().apply {
                userCourses?.forEach {
                    this[it.id] = it
                }
                userClubs?.forEach {
                    this[it.id] = it
                }
                Associate.getData(routines).forEach {
                    this[it.id] = it
                }
            }.values),
            click = fun(associate: Associate) {
                click(associate)
                dismiss()
            }
        )

        return binding.root
    }

}