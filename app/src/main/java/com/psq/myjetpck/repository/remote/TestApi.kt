package com.psq.myjetpck.repository.remote

import com.psq.myjetpck.model.TestBean
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 00:00
 * @desc   :
 */
interface TestApi {

    @GET("search/users")
    suspend fun getUsers(@Query("q") q: String): TestBean
}