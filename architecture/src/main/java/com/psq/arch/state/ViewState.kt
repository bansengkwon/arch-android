package com.psq.arch.state

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 14:44
 * @desc   :
 */

sealed class ViewState

class LoadingViewState(val charSequence: CharSequence?) : ViewState()

object ContentViewState : ViewState()

class ErrorViewState(val throwable: Throwable) : ViewState()

object NothingViewState : ViewState()