

package com.tejeet.beets.ui.binding_adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import com.tejeet.beets.R
import com.tejeet.beets.utils.NumbersUtils

@BindingAdapter("changeVideoLikedIcon")
fun ImageView.changeVideoLikedIcon(isLiked: Boolean) {
    val colorStateList = ResourcesCompat.getColorStateList(
        resources,
        if (isLiked) R.color.pinkBtnBackground else android.R.color.white,
        null
    )

    ImageViewCompat.setImageTintList(this, colorStateList)
}

@BindingAdapter("formatVideoCount")
fun TextView.formatVideoCount(count: Int) {
    text = NumbersUtils.formatCount(count)
}