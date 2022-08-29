package com.psq.arch.net

import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 21:56
 * @desc   :
 */

fun Throwable.parseErrorMessage(): String {
    return when (this) {
        is TimeoutException -> "网络请求超时"
        is IOException -> "网络异常"
        is HttpException -> {
            when (this.response()?.code()) {
                500 -> "服务器错误"
                404 -> "请求内容不存在"
                else -> this.response()?.message() ?: "未知异常"
            }
        }
        else -> this.message ?: "未知异常"
    }
}
