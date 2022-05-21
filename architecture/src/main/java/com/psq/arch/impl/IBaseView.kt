package com.psq.arch.impl

import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.gturedi.views.StatefulLayout

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 10:48
 * @desc   :
 */
interface IBaseView : IStateView {

    fun getContentView(): Int
    fun getStatefulLayout(): StatefulLayout?
    fun isVisibleStatefulLayout(): Boolean
    fun onErrorStateClickListener(view: View)

    override fun showToastView(charSequence: CharSequence) {
        ToastUtils.showShort(charSequence)
    }

    override fun showLoadingView(charSequence: CharSequence?) {
        getStatefulLayout()?.let {
            if (isVisibleStatefulLayout()) {
                if (charSequence != null) {
                    it.showLoading(charSequence.toString())
                } else {
                    it.showLoading()
                }
            }
        }
    }

    override fun showContentView() {
        getStatefulLayout()?.let {
            if (isVisibleStatefulLayout()) {
                it.showContent()
            }
        }
    }

    override fun showErrorView(throwable: Throwable) {
        getStatefulLayout()?.let {
            if (isVisibleStatefulLayout()) {
                it.showError(throwable.message) {
                    onErrorStateClickListener(it)
                }
            }
        }
    }
}