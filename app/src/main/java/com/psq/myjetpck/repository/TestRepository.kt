package com.psq.myjetpck.repository

import com.psq.myjetpck.repository.remote.TestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 18:17
 * @desc   :
 */

class TestRepository(val api: TestApi) {


    suspend fun getUsers(q: String) = api.getUsers(q)
}