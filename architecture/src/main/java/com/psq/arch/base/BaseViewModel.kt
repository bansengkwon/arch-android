package com.psq.arch.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psq.arch.impl.IStateView
import com.psq.arch.net.ApiResponse
import com.psq.arch.net.callFlow
import com.psq.arch.net.request
import com.psq.arch.state.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/12 10:07
 * @desc   :
 */
abstract class BaseViewModel : ViewModel(), IStateView {

    //显示toast信息
    val showToast: MutableLiveData<CharSequence> = MutableLiveData()

    //默认无状态
    val viewState: MutableStateFlow<ViewState> = MutableStateFlow(NothingViewState)


    override fun showToastView(charSequence: CharSequence) {
        showToast.postValue(charSequence)
    }

    override fun showLoadingView(charSequence: CharSequence?) {
        viewState.value = LoadingViewState(charSequence)
    }

    override fun showContentView() {
        viewState.value = ContentViewState
    }

    override fun showErrorView(throwable: Throwable) {
        viewState.value = ErrorViewState(throwable)
    }

    suspend fun <T> ApiResponse<T>.response(success: (T?) -> Unit) {
        this@response.request(
            start = {
                showLoadingView("加载中...")
            }, failure = {
                showErrorView(it)
            }, success = {
                showContentView()
                success(it)
            }
        )
    }

    suspend fun <T> T.execute(success: (T) -> Unit) {
        this@execute.callFlow(
            start = {
                showLoadingView("加载中...")
            }, failure = {
                showErrorView(it)
            }, success = {
                showContentView()
                success(it)
            }
        )
    }
}