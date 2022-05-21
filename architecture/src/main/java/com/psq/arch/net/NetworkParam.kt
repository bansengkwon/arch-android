package com.psq.arch.net

import okhttp3.ConnectionPool
import okhttp3.Interceptor
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

/**
 * @author : Anthony.Pan
 * @date   : 2022/5/21 21:06
 * @desc   :
 */
data class NetworkParam(
    val baseUrl: String,
    val connectTimeout: Long = 10,
    val readTimeout: Long = 10,
    val writeTimeout: Long = 10,
    val retryOnConnectionFailure: Boolean = false,
    val sslSocketFactory: SSLSocketFactory? = null,
    val trustManager: X509TrustManager? = null,
    val connectionPool: ConnectionPool? = null,
    val interceptors: List<Interceptor>? = null
)
