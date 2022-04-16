package com.pumpkin.pac_core.cache

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView

/**
 * WebViewCacheInterceptor
 *
 * @author pumpkin
 */
object WebViewCacheInterceptor : WebViewRequestInterceptor {

    @SuppressLint("StaticFieldLeak")
    var interceptorGlobal: WebViewCacheInterceptorGlobal? = null

    fun init(builder: WebViewCacheInterceptorGlobal.Builder, context: Context) {
        interceptorGlobal = builder.build(context)
    }

    override fun interceptRequest(request: WebResourceRequest?): WebResourceResponse? =
        interceptorGlobal?.interceptRequest(request)

    override fun interceptRequest(url: String?): WebResourceResponse? =
        interceptorGlobal?.interceptRequest(url)

    override fun loadUrl(webView: WebView, url: String) {
        interceptorGlobal?.loadUrl(webView, url)
    }

    override fun enableCache(enable: Boolean) {
        interceptorGlobal?.enableCache(enable)
    }
}