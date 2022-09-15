package com.psq.arch.state

/**
 * @author : Anthony.Pan
 * @date   : 2022/8/29 14:44
 * @desc   :
 */

sealed class TransitionStyle

object NothingStyle : TransitionStyle()

object LoadingDialogStyle : TransitionStyle()

object StateLayoutStyle : TransitionStyle()