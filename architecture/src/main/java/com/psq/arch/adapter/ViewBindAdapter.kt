package com.psq.arch.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.blankj.utilcode.util.SizeUtils

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/22 09:54
 * @desc   :
 */


@BindingAdapter(
    value = ["imageUrl", "placeholder", "error", "circleCrop", "roundedCorners", "corners"],
    requireAll = false
)
fun setImageUrl(
    imageView: ImageView,
    url: String?,
    placeholder: Drawable?,
    error: Drawable?,
    circleCrop: Boolean?,
    roundedCorners: Boolean?,
    corners: Int = 0
) {
    imageView.load(url) {
        crossfade(true)
        placeholder?.let {
            placeholder(it)
        }
        error?.let {
            error(it)
        }
        circleCrop?.let {
            if (it) {
                transformations(CircleCropTransformation())
            }
        }

        roundedCorners?.let {
            if (it) {
                val cornersParse = SizeUtils.dp2px(corners.toFloat()).toFloat()
                transformations(
                    RoundedCornersTransformation(
                        cornersParse,
                        cornersParse,
                        cornersParse,
                        cornersParse
                    )
                )
            }
        }
    }
}

@BindingAdapter(value = ["bindSrc"], requireAll = false)
fun setBindSrc(imageView: ImageView, @DrawableRes bindSrcId: Int) {
    imageView.setImageResource(bindSrcId)
}