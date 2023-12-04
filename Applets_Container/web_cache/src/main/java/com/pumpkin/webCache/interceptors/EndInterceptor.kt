package com.pumpkin.webCache.interceptors

import android.webkit.WebResourceResponse
import com.pumpkin.webCache.interceptor.Chain
import com.pumpkin.webCache.interceptor.Interceptor

class EndInterceptor : Interceptor {
    override fun intercept(chain: Chain): WebResourceResponse? = null
}