package com.pumpkin.webCache.base

import android.webkit.WebResourceResponse
import com.pumpkin.webCache.interceptor.Chain
import com.pumpkin.webCache.interceptor.Interceptor

abstract class DefaultInterceptor : Interceptor {
    override fun intercept(chain: Chain): WebResourceResponse? {
        return findResource(chain)
            ?: //try do next
            return chain.process()
    }

    protected abstract fun findResource(chain: Chain): WebResourceResponse?
}