package com.psq.arch.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.psq.arch.impl.IComponent
import com.psq.arch.impl.IBaseView

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/12 10:00
 * @desc   :
 */
abstract class BaseActivity<DB : ViewDataBinding, VM : BaseViewModel> :
    AppCompatActivity(), IBaseView, IComponent<DB, VM> {

    override val mDataBinding: DB by lazy { getDataBinding(this) }

    override val mViewModel: VM by lazy { getViewModel(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        register(lifecycleScope, this)
    }
}