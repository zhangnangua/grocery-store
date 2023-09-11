package com.pumpkin.pac_core.cache2

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 增加开启请求头缓存能力
 */
class HttpCacheInterceptor : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cache = request.header(CACHE_REQUEST_HEAD)
        val originResponse = chain.proceed(request)
        return if (!TextUtils.isEmpty(cache) && cache == CACHE_FALSE) {
            originResponse
        } else {
            //默认有效时间100一年
            originResponse.newBuilder().removeHeader("pragma").removeHeader("Cache-Control")
                .header("Cache-Control", "max-age=3153600000").build()
        }
    }

    companion object {
        const val CACHE_REQUEST_HEAD = "CACHE_REQUEST_KEY"
        const val CACHE_TRUE = "true"
        const val CACHE_FALSE = "false"
    }
}