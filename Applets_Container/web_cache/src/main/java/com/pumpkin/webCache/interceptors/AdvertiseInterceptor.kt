package com.pumpkin.webCache.interceptors

import android.webkit.WebResourceResponse
import com.pumpkin.webCache.base.DefaultInterceptor
import com.pumpkin.webCache.interceptor.Chain
import com.pumpkin.webCache.requestHelper.DefaultInterceptorConfig
import com.pumpkin.webCache.requestHelper.InterceptorConfig

/**
 * 广告拦截处理
 */
class AdvertiseInterceptor(private val c: InterceptorConfig = DefaultInterceptorConfig) : DefaultInterceptor() {

    override fun findResource(chain: Chain): WebResourceResponse? {
        return if (c.isAdvertise(chain.url)) {
            WebResourceResponse("text/plain", "utf-8", null)
        } else {
            null
        }
    }

    companion object {
        private val TAG = "AIR"
    }
}

