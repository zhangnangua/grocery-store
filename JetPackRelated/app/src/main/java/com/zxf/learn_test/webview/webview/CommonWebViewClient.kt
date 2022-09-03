package com.zxf.learn_test.webview.webview

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zxf.learn_test.webview.cache.WebViewCacheInterceptor

class CommonWebViewClient : WebViewClient() {

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        return WebViewCacheInterceptor.interceptRequest(request)
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        return WebViewCacheInterceptor.interceptRequest(url)
    }
}