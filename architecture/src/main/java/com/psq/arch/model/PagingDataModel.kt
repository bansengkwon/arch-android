package com.psq.arch.model

/**
 * @author : Anthony.Pan
 * @date   : 2022/9/6 10:48
 * @desc   :
 */
data class PagingDataModel(
    val isRefresh: Boolean,
    val hasNoMoreData: Boolean,
    val data: MutableList<Any>
)
