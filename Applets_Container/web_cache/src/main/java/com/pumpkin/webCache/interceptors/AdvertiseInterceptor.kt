package com.pumpkin.webCache.interceptors

import android.webkit.WebResourceResponse
import com.pumpkin.webCache.base.DefaultInterceptor
import com.pumpkin.webCache.domain.matcher.UrlMatcher
import com.pumpkin.webCache.interceptor.Chain
import com.pumpkin.webCache.requestHelper.DefaultInterceptorConfig
import com.pumpkin.webCache.requestHelper.InterceptorConfig

/**
 * 广告拦截处理
 */
class AdvertiseInterceptor(private val c: InterceptorConfig = DefaultInterceptorConfig) : DefaultInterceptor() {

    private val matcher = UrlMatcher()

    override fun findResource(chain: Chain): WebResourceResponse? {
        return if (c.isOtherInterceptor(chain.url) || (!chain.request.isForMainFrame && matcher.isBlock(chain.uri, chain.application.resources))) {
            WebResourceResponse("text/html", "UTF_8", null)
        } else {
            null
        }
    }

    companion object {
        private val TAG = "AIR"
    }
}

