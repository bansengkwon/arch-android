package com.psq.arch.impl

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 10:48
 * @desc   :
 */
interface IStateView {
    /**
     * 显示toast
     * @param charSequence CharSequence
     */
    fun showToast(charSequence: CharSequence)

    /**
     * 显示加载对话框
     * @param charSequence CharSequence
     */
    fun showLoadingDialog(charSequence: CharSequence?)

    /**
     *隐藏加载对话框
     */
    fun hideLoadingDialog()

    /**
     * 显示状态布局（StatefulLayout）加载
     * @param charSequence CharSequence?
     */
    fun showLoadingView(charSequence: CharSequence?)

    /**
     * 显示状态布局(StatefulLayout)内容
     */
    fun showContentView()

    /**
     * 显示状态布局(StatefulLayout)错误
     * @param throwable Throwable
     */
    fun showErrorView(throwable: Throwable)
}