package com.naltynbekkz.nulife.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Contact
import com.naltynbekkz.nulife.model.Request
import com.naltynbekkz.nulife.ui.market.front.RequestsAdapter
import com.naltynbekkz.nulife.util.contacts.ContactsAdapter
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlin.math.round


@BindingAdapter("setIcon")
fun setIcon(view: View, icon: Int?) {
    if (icon != null) {
        view.background = view.context.resources.getDrawable(icon, view.context.theme)
    }
}

@BindingAdapter("setIcon")
fun setIcon(button: Button, icon: Int?) {
    if (icon != null) {
        button.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0)
    }
}

@BindingAdapter("endDrawable")
fun setDrawableEnd(button: AppCompatButton, drawable: Drawable?) {
    if (drawable != null) {
        button.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    } else {
        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }
}


@BindingAdapter("imageUrl")
fun imageUrl(image: ImageView, images: ArrayList<String>?) {
    if (images != null) {
        Glide.with(image.context)
            .load(images.firstOrNull())
            .into(image)
    }
}

@BindingAdapter("logoUrl")
fun logoUrl(image: ImageView, logo: String?) {
    if (logo != null && logo.isNotEmpty()) {
        Glide.with(image.context)
            .load(logo)
            .into(image)
    }
}

@BindingAdapter("logoUrlWithZoom")
fun logoUrlWithZoom(image: ImageView, logo: String?) {
    if (logo != null && logo.isNotEmpty()) {
        Glide.with(image.context)
            .load(logo)
            .into(image)
        image.setOnClickListener {
            StfalconImageViewer.Builder(
                image.context,
                listOf(logo)
            ) { view, image ->
                Glide.with(view.context)
                    .load(image)
                    .into(view)
            }
                .allowZooming(true)
                .withTransitionFrom(image)
                .withHiddenStatusBar(false)
                .show()
        }
    }
}


@BindingAdapter("layout_width")
fun layoutWidth(view: View, width: Int) {
    if (width != 0) {
        view.layoutParams.width = width
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("visibility")
fun setVisibility(view: View, visibility: Boolean) {
    if (visibility) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}


// CLUBS

@BindingAdapter("color")
fun setColor(view: View, color: String) {
    view.setBackgroundColor(Color.parseColor(color))
}

@BindingAdapter("imageUri")
fun setUri(image: ImageView, uri: Uri) {
    Glide.with(image.context)
        .load(uri)
        .into(image)
}

@BindingAdapter("android:text")
fun setText(view: TextView, long: Long?) {
    if (long != null) {
        view.text = long.toString()
    }
}

@BindingAdapter("app:text")
fun setText(view: TextView, float: Float?) {
    if (float != null) {
        view.text = (round(float * 10.0) / 10.0).toString()

    }
}

@InverseBindingAdapter(attribute = "android:text")
fun getText(view: TextView): Long? {
    return if (view.text.toString().isEmpty()) {
        0L
    } else {
        view.text.toString().toLong()
    }
}

@BindingAdapter("strikethrough")
fun strikethrough(view: TextView, show: Boolean) {
    view.paintFlags = if (show) {
        view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        view.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}

@BindingAdapter("contactsAdapter")
fun setContactsAdapter(view: RecyclerView, contacts: ArrayList<Contact>?) {
    contacts?.let {
        view.adapter = ContactsAdapter { contact ->
            when (contact.type) {
                Contact.LOCATION -> {
                    (view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
                        ClipData.newPlainText("address", contact.data)
                    )
                    Toast.makeText(view.context, "Location copied.", Toast.LENGTH_SHORT).show()
                }
                Contact.PHONE -> {
                    val callIntent = Intent(Intent.ACTION_DIAL)
                    callIntent.data = Uri.parse("tel:${contact.data}")
                    view.context.startActivity(callIntent)
                }
                Contact.EMAIL -> {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:${contact.data}")
                    if (intent.resolveActivity(view.context.packageManager) != null) {
                        view.context.startActivity(intent)
                    }
                }
                Contact.VK -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://vk.com/${contact.data}")
                    view.context.startActivity(intent)
                }
                Contact.INSTAGRAM -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://instagram.com/${contact.data}")
                    view.context.startActivity(intent)
                }
                Contact.TELEGRAM -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://t.me/${contact.data}")
                    view.context.startActivity(intent)
                }
                Contact.FACEBOOK -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://www.facebook.com/${contact.data}")
                    view.context.startActivity(intent)
                }
                else -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("http://${contact.data}")
                    view.context.startActivity(intent)
                }
            }

        }.apply {
            submitList(contacts)
        }
    }
}

@BindingAdapter("imagesAdapter")
fun setImagesAdapter(view: ViewPager, images: ArrayList<String>?) {
    images?.let {
        view.adapter =
            ImagePagerAdapter(
                view.context,
                images
            )
    }
}

@BindingAdapter("viewPager")
fun setViewPager(view: TabLayout, viewPager: ViewPager?) {
    viewPager?.let { view.setupWithViewPager(viewPager, true) }
}

@BindingAdapter("requestsAdapter")
fun setRequestsAdapter(view: RecyclerView, requests: ArrayList<Request>?) {
    requests?.let { view.adapter = RequestsAdapter(requests) }
}

@BindingAdapter("layout_height")
fun setLayoutHeight(view: View, height: Float) {
    val layoutParams = view.layoutParams
    layoutParams.height = height.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("errorMessage")
fun setErrorMessage(view: TextInputLayout, loading: Boolean?) {
    view.isErrorEnabled = true
    if (view.editText?.text.toString().trim().isEmpty() && loading != null && loading == false) {
        view.error = view.context.resources.getString(R.string.field_empty_error_message)
    } else {
        view.error = null
    }
}