package com.naltynbekkz.nulife.util.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.LayoutRatingBinding
import com.naltynbekkz.nulife.util.Convert


class RatingView : ConstraintLayout {

    private lateinit var binding: LayoutRatingBinding

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context!!, attrs, defStyle) {
        initView(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?
    ) : super(context!!, attrs) {
        initView(attrs)
    }

    constructor(context: Context?) : super(context!!) {
        initView(null)
    }

    private fun initView(attrs: AttributeSet?) {

        binding =
            LayoutRatingBinding.inflate(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        inflate(context, R.layout.layout_rating, this)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.RatingView)
        try {
            val five = ta.getInteger(R.styleable.RatingView_five_stars, 0)
            val four = ta.getInteger(R.styleable.RatingView_four_stars, 0)
            val three = ta.getInteger(R.styleable.RatingView_three_stars, 0)
            val two = ta.getInteger(R.styleable.RatingView_two_stars, 0)
            val one = ta.getInteger(R.styleable.RatingView_one_stars, 0)

            val array = arrayOf(one, two, three, four, five)
            val max = array.max()!!
            binding.oneSize = if (max != 0) Convert.dpToPx(112 * array[0] / max) else 0
            binding.twoSize = if (max != 0) Convert.dpToPx(112 * array[1] / max) else 0
            binding.threeSize = if (max != 0) Convert.dpToPx(112 * array[2] / max) else 0
            binding.fourSize = if (max != 0) Convert.dpToPx(112 * array[3] / max) else 0
            binding.fiveSize = if (max != 0) Convert.dpToPx(112 * array[4] / max) else 0

            binding.one = array[0].toString()
            binding.two = array[1].toString()
            binding.three = array[2].toString()
            binding.four = array[3].toString()
            binding.five = array[4].toString()

            val count = array.sum()
            binding.ratingCount = count
            binding.rating = if (count == 0) {
                0f
            } else {
                (five * 5 + four * 4 + three * 3 + two * 2 + one * 1).toFloat() / count
            }


        } finally {
            ta.recycle()
        }
    }


}