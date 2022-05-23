package com.psq.arch.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.psq.arch.impl.ArchComponent

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/12 10:10
 * @desc   :
 */
abstract class BaseFragment<DB : ViewDataBinding, VM : BaseViewModel> : Fragment(),
    ArchComponent<DB, VM> {

    override val mDataBinding: DB by lazy { getDataBinding(this) }
    override val mViewModel: VM by lazy { getViewModel(this) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindArchComponent(lifecycleScope, this)
    }

}