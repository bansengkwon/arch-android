package com.psq.arch.net

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 22:22
 * @desc   :
 */


data class ApiResponse<T>(
    val data: T?,
    val code: Int?,
    val message: String?
) {
    val isSuccess: Boolean
        get() = code == 200
}

