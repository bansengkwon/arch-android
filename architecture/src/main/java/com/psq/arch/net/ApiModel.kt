package com.psq.arch.net

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 22:22
 * @desc   :
 */

data class ApiResp<out Result, out Meta>(val data: Result?, val meta: Meta?)

