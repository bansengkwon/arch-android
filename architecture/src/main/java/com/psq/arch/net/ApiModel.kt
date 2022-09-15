package com.psq.arch.net

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 22:22
 * @desc   :
 */


class NetResult<T>(
    val data: T?,
    val requestId: String?,
    val time: String?,
    val status: String?,
    val error: NetError?,
    val orderId: String?
) {
    val isSuccess: Boolean
        get() {
            return status == "success"
        }
}

data class NetError(
    val code: Int?,
    val message: String?
)

abstract class PageResultBase<T, E> {
    var data: ArrayList<T>? = null
    var meta: E? = null
}

open class PageResult<T> : PageResultBase<T, MetaEntity>() {
    val hasMore: Boolean
        get() {
            return meta != null && meta?.hasMore ?: false
        }
}

open class PageMetaResult<T, Meta : MetaEntity> : PageResultBase<T, Meta>() {

    val hasMore: Boolean
        get() {
            return meta != null && meta?.hasMore ?: false
        }
}


