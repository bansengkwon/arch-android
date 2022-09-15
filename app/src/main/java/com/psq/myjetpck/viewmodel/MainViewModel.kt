package com.psq.myjetpck.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.psq.arch.base.BaseViewModel
import com.psq.myjetpck.model.TestBean
import com.psq.myjetpck.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    var repository: TestRepository
) : BaseViewModel() {

    val data: MutableLiveData<TestBean> = MutableLiveData()

    fun getUsers(q: String = "aa") {
        viewModelScope.launch {
            repository.getUsers(q)
                .dispatcherIO()
                .withState(true)
                .onCatch()
                .onCollect {
                    data.postValue(it)
                }
        }
    }


}

