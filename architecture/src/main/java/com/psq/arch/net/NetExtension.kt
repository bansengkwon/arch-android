package com.psq.arch.net

import com.blankj.utilcode.util.GsonUtils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

/**
 * @author : Anthony.Pan
 * @date   : 2022/8/30 10:08
 * @desc   :
 */

fun Any.toRequestBody(): RequestBody {
    return GsonUtils.toJson(this).toRequestBody("application/json;charset=UTF-8".toMediaTypeOrNull())
}

// 解决@Multipart带参，使用上面这个会将string参数转换为 "string"导致签名异常的问题
fun String.toRequestBody(toMediaTypeOrNull: MediaType?): RequestBody {
    return RequestBody.create("application/json;charset=UTF-8".toMediaTypeOrNull(), this)
}