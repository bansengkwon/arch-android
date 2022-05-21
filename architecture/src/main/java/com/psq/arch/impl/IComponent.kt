package com.psq.arch.impl

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.psq.arch.base.BaseViewModel
import com.psq.arch.state.ContentViewState
import com.psq.arch.state.ErrorViewState
import com.psq.arch.state.LoadingViewState
import com.psq.arch.state.NothingViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType


/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 10:48
 * @desc   :
 */
interface IComponent<DB : ViewDataBinding, VM : BaseViewModel> : IBaseView {

    val mDataBinding: DB
    val mViewModel: VM

    fun getDataBinding(activity: AppCompatActivity): DB =
        DataBindingUtil.setContentView<DB>(activity, getContentView()).apply {
            lifecycleOwner = activity
        }

    fun getDataBinding(fragment: Fragment): DB =
        DataBindingUtil.inflate<DB>(fragment.layoutInflater, getContentView(), null, false).apply {
            lifecycleOwner = fragment.viewLifecycleOwner
        }

    fun getViewModel(owner: ViewModelStoreOwner): VM {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        return ViewModelProvider(owner)[type[1] as Class<VM>]
    }

    fun register(scope: CoroutineScope, owner: LifecycleOwner) {
        bindViewModelToDataBinding()
        observeViewModel(scope, owner)
    }

    private fun bindViewModelToDataBinding() {
        try {
            val setViewMethod: Method? =
                mDataBinding.javaClass.getDeclaredMethod("setViewModel", mViewModel.javaClass)
            setViewMethod?.apply {
                isAccessible = true
                invoke(mDataBinding, mViewModel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeViewModel(scope: CoroutineScope, owner: LifecycleOwner) {
        mViewModel.showToast.observe(owner) {
            showToastView(it)
        }
        scope.launch {
            mViewModel.viewState.collect {
                when (it) {
                    is LoadingViewState -> {
                        showLoadingView(it.charSequence)
                    }
                    is ContentViewState -> {
                        showContentView()
                    }
                    is ErrorViewState -> {
                        showErrorView(it.throwable)
                    }
                    is NothingViewState -> {}
                }
            }
        }
    }
}