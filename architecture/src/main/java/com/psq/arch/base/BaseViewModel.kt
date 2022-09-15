package com.psq.arch.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.psq.arch.impl.IStateView
import com.psq.arch.model.EmptyWrapper
import com.psq.arch.model.PagingDataModel
import com.psq.arch.net.*
import com.psq.arch.state.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.concurrent.CancellationException

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/12 10:07
 * @desc   :
 */
abstract class BaseViewModel : ViewModel(), IStateView {

    //显示toast信息
    val mShowToast: MutableLiveData<CharSequence> = MutableLiveData()


    //默认无状态
    val mStatefulLayout: MutableLiveData<ViewState> = MutableLiveData()

    //默认无加载对话框状态
    val mLoadingDialogState: MutableLiveData<ViewState> = MutableLiveData()

    //是否刷新
    var isRefresh: Boolean = false

    //是分页数据
    var isPagingData: Boolean = false

    //用于分页加载时采用发送失败或成功数据
    val callback: MutableLiveData<Result<*>> = MutableLiveData()

    //用于接受分页列表集合数据
    val result: MutableList<Any> = mutableListOf()

    //当前页
    var page: Int = 1

    //默认每一加载20条数据
    var perPage = 20

    //是否显示状态布局
    var showState: Boolean = false

    private var style: TransitionStyle = NothingStyle


    override fun showToast(charSequence: CharSequence?) {
        mShowToast.postValue(charSequence)
    }

    override fun showLoadingDialog(charSequence: CharSequence?) {
        style = LoadingDialogStyle
        mLoadingDialogState.postValue(ShowLoadingDialogState(charSequence))
    }

    override fun hideLoadingDialog() {
        style = NothingStyle
        mLoadingDialogState.postValue(HideLoadingDialogState)
    }


    override fun showLoadingView(charSequence: CharSequence?) {
        if (showState) {
            style = StateLayoutStyle
            mStatefulLayout.postValue(LoadingViewState(charSequence))
        }
    }

    override fun showContentView() {
        style = NothingStyle
        mStatefulLayout.postValue(ContentViewState)
    }

    override fun showErrorView(throwable: Throwable) {
        style = NothingStyle
        mStatefulLayout.postValue(ErrorViewState(throwable))
    }


    fun <T> Flow<T>.dispatcherIO() = this.flowOn(Dispatchers.IO)
    fun <T> Flow<T>.dispatcherMain() = this.flowOn(Dispatchers.Main)
    fun <T> Flow<T>.withLoading() = this.onStart { showLoadingDialog() }
    fun <T> Flow<T>.withState(show: Boolean = true) = this.onStart {
        showState = show
        showLoadingView()
    }

    /**
     * 异常处理
     * @receiver Flow<T>
     * @param pagingData Boolean 是否分页数据，主要用来停止刷新作用
     * @param needLogin Boolean 默认都是需要登录
     * @param failure Function1<Throwable, Unit>
     * @return Flow<T>
     */
    fun <T> Flow<T>.onCatch(
        pagingData: Boolean = false,
        needLogin: Boolean = true,
        showToast: Boolean = true,
        failure: (Throwable) -> Unit = {}
    ) =
        this.catch { e ->
            if (style == LoadingDialogStyle) {
                hideLoadingDialog()
                if (showToast) {
                    showToast(e.message)
                }
            }
            if (style == StateLayoutStyle) {
                showErrorView(e)
            }
            val throwable = (e as? CancellationException)?.cause ?: e
            failure.invoke(throwable)
            isPagingData = pagingData
            if (isPagingData) {
                postFailure(throwable)
            }
            (throwable as? ApiException)?.needLogin(needLogin)
        }


    suspend fun <T> Flow<T>.onCollect(success: (T) -> Unit) {
        this.collect {
            if (style == LoadingDialogStyle) {
                hideLoadingDialog()
            }
            if (style == StateLayoutStyle) {
                showContentView()
            }
            success.invoke(it)
        }
    }


    //----------------------------------------------------------------------------------------------

    /**
     * 发送异常数据
     * @receiver MutableLiveData<Result<*>>
     * @param throwable Throwable
     */
    fun postFailure(throwable: Throwable) {
        callback.postValue(Result.failure<Throwable>(throwable))
    }

    /**
     * 发送成功数据
     * @receiver MutableLiveData<Result<*>>
     * @param data T
     */
    fun <T> postSuccess(data: T) {
        callback.postValue(Result.success(data))
    }

    /**
     * 针对通用的分页加载数据时采用这个发送通知更新页面
     * @receiver NetResult<PageResult<T>>
     * @param emptyWrapper EmptyWrapper
     */
    fun <T : Any> NetResult<PageResult<T>>.postPagingData(emptyWrapper: EmptyWrapper = EmptyWrapper()) {
        //表示没有更多数据
        val hasNoMoreData = this.data?.hasMore == false
        //获取当前接口返回的分页数据
        val data: List<T> = this.data?.data ?: listOf()
        result.apply {
            if (isRefresh) {
                clear()
            }
            if (data.isNotEmpty()) {
                addAll(data)
            }
            if (isRefresh && data.isEmpty()) {
                add(emptyWrapper)
            }
        }
        page++
        postSuccess(PagingDataModel(isRefresh, hasNoMoreData, result))
    }

    /**
     * 针对需要从Meta获取数据
     * @receiver NetResult<PageMetaResult<T, Meta>>
     * @param emptyWrapper EmptyWrapper
     */
    fun <T : Any, Meta : MetaEntity> NetResult<PageMetaResult<T, Meta>>.postPagingDataAndMeta(
        emptyWrapper: EmptyWrapper = EmptyWrapper()
    ) {
        //表示没有更多数据
        val hasNoMoreData = this.data?.hasMore == false
        //获取当前接口返回的分页数据
        val data: List<T> = this.data?.data ?: listOf()
        result.apply {
            if (isRefresh) {
                clear()
            }
            if (data.isNotEmpty()) {
                addAll(data)
            }
            if (isRefresh && data.isEmpty()) {
                add(emptyWrapper)
            }
        }
        page++
        postSuccess(PagingDataModel(isRefresh, hasNoMoreData, result))
    }
}