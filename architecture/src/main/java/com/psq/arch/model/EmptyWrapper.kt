package com.psq.arch.model

import com.psq.architecture.R


data class EmptyWrapper(
    val content: CharSequence = "暂无相关内容",
    val emptyImg: Int = R.mipmap.empty,
    val heightDp: Int = 0,
    val backgroundColor: Int = R.color.transparent,
    val fontSize: Int = 16
)