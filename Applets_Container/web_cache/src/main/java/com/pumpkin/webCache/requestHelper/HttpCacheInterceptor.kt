package com.pumpkin.webCache.requestHelper

import android.util.Log
import androidx.viewbinding.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 增加开启请求头缓存能力
 */
class HttpCacheInterceptor(private val c: InterceptorConfig) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originResponse = chain.proceed(request)
        return if (!c.isCache(request.url.toString())) {

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "intercept () -> url is ${request.url} , code is ${originResponse.code} ,no cache")
            }

            originResponse
        } else {

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "intercept () -> url is ${request.url} , code is ${originResponse.code} , cache ")
            }


            originResponse.newBuilder().removeHeader("pragma").removeHeader("Cache-Control")
                .header("Cache-Control", c.cacheDay()).build()
        }
    }

    companion object {
        const val CACHE_REQUEST_HEAD = "CACHE_REQUEST_KEY"
        const val CACHE_TRUE = "true"
        const val CACHE_FALSE = "false"

        private const val TAG = "HttpCacheInterceptor"
    }
}