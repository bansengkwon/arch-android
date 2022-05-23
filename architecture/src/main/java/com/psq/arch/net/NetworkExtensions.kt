package com.psq.arch.net

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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

suspend fun <T> T.callFlow(
    start: () -> Unit,
    failure: (Throwable) -> Unit,
    success: (T) -> Unit
) = flow {
    emit(this@callFlow)
}.flowOn(Dispatchers.IO)
    .onStart {
        start.invoke()
    }
    .catch { e ->
        failure.invoke(e)
    }.collect {
        success(it)
    }


suspend fun <T> ApiResponse<T>.request(
    start: () -> Unit,
    failure: (Throwable) -> Unit,
    success: (T?) -> Unit
) = callFlow(
    start = {
        start.invoke()
    },
    failure = {
        failure.invoke(it)
    },
    success = {
        if (it.isSuccess) {
            success.invoke(it.data)
        } else {
            failure.invoke(Throwable(it.message))
        }
    })