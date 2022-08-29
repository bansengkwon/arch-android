package com.psq.arch.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psq.arch.impl.IStateView
import com.psq.arch.net.ApiException
import com.psq.arch.net.NetResult
import com.psq.arch.state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/12 10:07
 * @desc   :
 */
abstract class BaseViewModel : ViewModel(), IStateView {

    //显示toast信息
    val mShowToast: MutableLiveData<CharSequence> = MutableLiveData()

    //默认无状态
    val mStatefulLayout: MutableStateFlow<ViewState> = MutableStateFlow(NothingViewState)

    //默认无加载对话框状态
    val mLoadingDialogState: MutableStateFlow<ViewState> = MutableStateFlow(NothingViewState)


    override fun showToast(charSequence: CharSequence) {
        mShowToast.postValue(charSequence)
    }

    override fun showLoadingDialog(charSequence: CharSequence?) {
        mLoadingDialogState.value = ShowLoadingDialogState(charSequence)
    }

    override fun hideLoadingDialog() {
        mLoadingDialogState.value = HideLoadingDialogState
    }


    override fun showLoadingView(charSequence: CharSequence?) {
        mStatefulLayout.value = LoadingViewState(charSequence)
    }

    override fun showContentView() {
        mStatefulLayout.value = ContentViewState
    }

    override fun showErrorView(throwable: Throwable) {
        mStatefulLayout.value = ErrorViewState(throwable)
    }


    /**
     * 网络请求
     * @param flow Flow<NetResult<T>>
     * @param style TransitionStyle
     * @param success Function1<T?, Unit>
     * @param failure Function1<Throwable, Unit>
     */
    fun <T> request(
        flow: Flow<NetResult<T>>,
        style: TransitionStyle = NothingStyle,
        success: (T?) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            flow.flowOn(Dispatchers.IO)
                .onStart {
                    when (style) {
                        is LoadingDialogStyle -> {
                            showLoadingDialog(style.charSequence)
                        }
                        is StateLayoutStyle -> {
                            showLoadingView(style.charSequence)
                        }
                        else -> {}
                    }
                }
                .catch { e ->
                    when (style) {
                        is LoadingDialogStyle -> {
                            hideLoadingDialog()
                        }
                        is StateLayoutStyle -> {
                            showErrorView(e)
                        }
                        else -> {}
                    }
                    failure.invoke(e)
                }
                .onCompletion {
                    //todo 以后可能会用到
                }
                .collect {
                    if(style is LoadingDialogStyle){
                        hideLoadingDialog()
                    }
                    it.error?.let {
                        val apiException = ApiException(it)
                        if (style is StateLayoutStyle) {
                            showErrorView(apiException)
                        }
                        failure.invoke(apiException)
                    } ?: success.invoke(it.data)
                }
        }
    }
}