package com.zxf.learn_test.webview.cache

import android.text.TextUtils
import com.zxf.learn_test.webview.cache.CACHE_REQUEST_HEAD
import com.zxf.learn_test.webview.cache.CacheType
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 作者： 张先锋
 * 创建时间： 2022/03/17 15:31
 * 版本： [1.0, 2022/03/17]
 * 版权： 国泰新点软件股份有限公司
 * 描述： 相应头设置过期时间
 */
class HttpCacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val cache = request.header(CACHE_REQUEST_HEAD)
        val originResponse = chain.proceed(request)
        return if (!TextUtils.isEmpty(cache) && cache == CacheType.NOCACHE.name) {
            originResponse
        } else {
            //默认有效时间1年
            originResponse.newBuilder().removeHeader("pragma").removeHeader("Cache-Control")
                .header("Cache-Control", "max-age=31536000").build()
        }
    }
}