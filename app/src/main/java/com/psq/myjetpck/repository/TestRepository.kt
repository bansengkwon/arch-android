package com.psq.myjetpck.repository

import com.psq.myjetpck.repository.remote.TestApi
import com.psq.myjetpck.model.TestBean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 18:17
 * @desc   :
 */

class TestRepository(val api: TestApi) {


    fun getUsers(q: String): Flow<Result<TestBean>> {
        return flow {
            val result = api.getUsers(q)
            emit(Result.success(result))
        }
    }
}