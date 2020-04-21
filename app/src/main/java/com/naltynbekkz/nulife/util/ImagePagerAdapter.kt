package com.naltynbekkz.nulife.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer

class ImagePagerAdapter(
    private val context: Context,
    private val images: ArrayList<String>
) : PagerAdapter() {

    private lateinit var viewer: StfalconImageViewer<String>

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun getCount() = images.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = ImageView(context)

        Glide.with(context)
            .load(images[position])
            .into(view)

        view.setOnClickListener {
            viewer = StfalconImageViewer.Builder(
                view.context,
                images
            ) { view, image ->
                Glide.with(view.context)
                    .load(image)
                    .into(view)
            }
                .allowZooming(true)
                .withStartPosition(position)
                .withTransitionFrom(view)
                .withHiddenStatusBar(false)
                .withImageChangeListener {
                    viewer.updateTransitionImage(if (it != position) null else view)
                }
                .show()
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}