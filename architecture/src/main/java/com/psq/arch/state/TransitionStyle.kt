package com.psq.arch.state

/**
 * @author : Anthony.Pan
 * @date   : 2022/8/29 14:44
 * @desc   :
 */

sealed class TransitionStyle

object NothingStyle : TransitionStyle()

data class LoadingDialogStyle(val charSequence: CharSequence? = null) : TransitionStyle()

data class StateLayoutStyle(val charSequence: CharSequence? = null) : TransitionStyle()