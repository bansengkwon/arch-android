package com.psq.arch.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
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
object ViewBindAdapter {

    @BindingAdapter(
        value = ["imageUrl", "placeholder", "error", "circleCrop", "roundedCorners", "corners"],
        requireAll = false
    )
    @JvmStatic
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
}