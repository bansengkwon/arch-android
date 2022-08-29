package com.psq.arch.di

import com.hjq.gson.factory.GsonFactory
import com.psq.arch.net.NetworkParam
import com.zyj.retrofit.adapter.FlowCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/20 23:07
 * @desc   :
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideOkHttpClient(param: NetworkParam): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (param.sslSocketFactory != null && param.trustManager != null) {
                sslSocketFactory(param.sslSocketFactory, param.trustManager)
            }
            connectTimeout(param.connectTimeout, TimeUnit.SECONDS)
            readTimeout(param.readTimeout, TimeUnit.SECONDS)
            writeTimeout(param.writeTimeout, TimeUnit.SECONDS)
            retryOnConnectionFailure(param.retryOnConnectionFailure)
            param.connectionPool?.let {
                connectionPool(it)
            }
            param.interceptors?.forEach {
                addInterceptor(it)
            }
        }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(param: NetworkParam, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(param.baseUrl)
            .addCallAdapterFactory(FlowCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonFactory.getSingletonGson()))
            .build()
    }
}