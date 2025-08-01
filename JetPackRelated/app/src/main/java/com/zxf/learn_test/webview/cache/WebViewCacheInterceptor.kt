package com.zxf.learn_test.webview.cache

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView


object WebViewCacheInterceptor : IWebViewRequestInterceptor {

    @SuppressLint("StaticFieldLeak")
    var interceptorGlobal: WebViewCacheInterceptorGlobal? = null


    fun init(builder: WebViewCacheInterceptorGlobal.Builder) {
        interceptorGlobal = builder.build()
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
