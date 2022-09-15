package com.psq.arch.impl

import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.fansmall.helper.SimpleHUD
import com.gturedi.views.StatefulLayout
import com.psq.arch.net.parseErrorMessage

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 10:48
 * @desc   :
 */
interface IBaseView : IStateView {
    /**
     * 获取布局
     * @return Int
     */
    fun getContentView(): Int

    /**
     *
     * @return StatefulLayout?
     */
    fun getStatefulLayout(): StatefulLayout?

    /**
     * 来自StatefulLayout错误展示页面的点击监听操作
     * @param view View
     */
    fun onErrorStateClickListener(view: View)

    override fun showToast(charSequence: CharSequence?) {
        ToastUtils.showShort(charSequence)
    }

    override fun showLoadingView(charSequence: CharSequence?) {
        getStatefulLayout()?.let {
            if (charSequence != null) {
                it.showLoading(charSequence.toString())
            } else {
                it.showLoading()
            }
        }
    }

    override fun showContentView() {
        getStatefulLayout()?.showContent()
    }

    override fun showErrorView(throwable: Throwable) {
        getStatefulLayout()?.showError(throwable.parseErrorMessage()) {
            onErrorStateClickListener(it)
        }
    }

    override fun hideLoadingDialog() {
        SimpleHUD.dismiss()
    }
}