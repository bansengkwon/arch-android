package com.psq.arch.state

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 14:44
 * @desc   :
 */

sealed class ViewState

/**
 * 无状态
 */
object NothingViewState : ViewState()

/**
 * StatefulLayout-加载状态
 * @property charSequence CharSequence?
 * @constructor
 */
class LoadingViewState(val charSequence: CharSequence?) : ViewState()

/**
 * StatefulLayout-内容状态
 */
object ContentViewState : ViewState()

/**
 * StatefulLayout-错误状态
 * @property throwable Throwable
 * @constructor
 */
class ErrorViewState(val throwable: Throwable) : ViewState()

/**
 * 显示加载对话框
 */
class ShowLoadingDialogState (val charSequence: CharSequence?): ViewState()

/**
 * 隐藏加载对话框
 */
object HideLoadingDialogState : ViewState()