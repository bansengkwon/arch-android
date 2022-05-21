package com.psq.myjetpck.viewmodel

import androidx.lifecycle.MutableLiveData
import com.psq.arch.base.BaseViewModel
import com.psq.myjetpck.model.TestBean
import com.psq.myjetpck.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    var repository: TestRepository
) : BaseViewModel() {

    val result: MutableLiveData<TestBean> = MutableLiveData()


    fun getUsers(q: String = "aa") {
        repository.getUsers(q).request {
            result.postValue(it)
        }
    }

}

