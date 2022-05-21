package com.psq.arch.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.psq.arch.impl.IStateView
import com.psq.arch.net.execute
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


    fun <T> Flow<Result<T>>.request(
        showStateView: Boolean = true,
        block: (T) -> Unit
    ) {
        viewModelScope.launch {
            this@request.execute(
                start = {
                    if (showStateView) {
                        showLoadingView("加载中...")
                    }
                },
                success = { result ->
                    if (showStateView) {
                        showContentView()
                    }
                    block.invoke(result)
                },
                failure = { e ->
                    if (showStateView) {
                        showErrorView(e)
                    }
                }
            )
        }
    }
}