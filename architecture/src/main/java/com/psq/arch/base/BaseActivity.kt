package com.psq.arch.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.fansmall.helper.SimpleHUD
import com.gyf.immersionbar.ImmersionBar
import com.psq.arch.impl.ArchComponent
import com.psq.arch.impl.IBaseView
import com.psq.architecture.R

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/12 10:00
 * @desc   :
 */
abstract class BaseActivity<DB : ViewDataBinding, VM : BaseViewModel> :
    AppCompatActivity(), ArchComponent<DB, VM> {

    override val mDataBinding: DB by lazy { getDataBinding(this) }

    override val mViewModel: VM by lazy { getViewModel(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindArchComponent(lifecycleScope, this)
        initImmersionBar()
    }

    private fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarDarkFont(isStatusBarDarkFont())   //状态栏字体是深色，不写默认为亮色
            .navigationBarColor(R.color.white)
            .autoNavigationBarDarkModeEnable(true, 0.2f)
            .init()
    }

    fun isStatusBarDarkFont(): Boolean = true

    override fun showLoadingDialog(charSequence: CharSequence?) {
        SimpleHUD.show(this)
    }
}