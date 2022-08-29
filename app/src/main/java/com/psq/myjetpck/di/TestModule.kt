package com.psq.myjetpck.di

import com.psq.arch.net.HttpsUtils
import com.psq.arch.net.NetworkParam
import com.psq.myjetpck.BuildConfig
import com.psq.myjetpck.repository.TestRepository
import com.psq.myjetpck.repository.remote.TestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 18:38
 * @desc   :
 */
@Module
@InstallIn(SingletonComponent::class)
object TestModule {


    @Provides
    @Singleton
    fun provideNetWorkParam(): NetworkParam {
        val sslFactory = HttpsUtils.getSslSocketFactory(null, null, null)
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(
                if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            )
        val interceptors = arrayListOf<Interceptor>(loggingInterceptor)
        return NetworkParam(
            baseUrl = "https://api.github.com/",
            interceptors = interceptors,
            sslSocketFactory = sslFactory.sSLSocketFactory,
            trustManager = sslFactory.trustManager,
            connectionPool = ConnectionPool(0, 1, TimeUnit.NANOSECONDS)
        )
    }

    @Provides
    @Singleton
    fun provideTestRepository(api: TestApi): TestRepository {
        return TestRepository(api)
    }

    @Singleton
    @Provides
    fun provideTestApi(retrofit: Retrofit): TestApi {
        return retrofit.create(TestApi::class.java)
    }
}