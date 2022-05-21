package com.psq.arch.impl

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 10:48
 * @desc   :
 */
interface IStateView {

    fun showToastView(charSequence: CharSequence)
    fun showLoadingView(charSequence: CharSequence?)
    fun showContentView()
    fun showErrorView(throwable: Throwable)
}