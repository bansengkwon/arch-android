package com.psq.arch.net

/**
 * @author : Anthony.Pan
 * @date   : 2022/8/29 15:17
 * @desc   :
 */
class ApiException(error:NetError):RuntimeException(error.message)