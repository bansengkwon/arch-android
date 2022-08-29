package com.psq.myjetpck.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.psq.arch.base.BaseViewModel
import com.psq.myjetpck.model.TestBean
import com.psq.myjetpck.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    var repository: TestRepository
) : BaseViewModel() {

    val result: MutableLiveData<TestBean> = MutableLiveData()

    fun getUsers(q: String = "aa") {
        viewModelScope.launch {

//            repository.getUsers(q).request()

            repository.getUsers(q)
                .flowOn(Dispatchers.IO)
                .onStart {
                    showLoadingView(null)
                }.catch { e ->

                }
                .onCompletion {

                }
                .collect {
                    showContentView()
                    result.postValue(it)
                }
        }
    }



}

