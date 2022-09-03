package com.zxf.learn_test.webview.cache

import android.annotation.SuppressLint
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView

/**
 * 作者： 张先锋
 * 创建时间： 2022/03/17 15:31
 * 版本： [1.0, 2022/03/17]
 * 版权： 国泰新点软件股份有限公司
 * 描述： WebViewRequestInterceptor拦截器功能具体实现类
 */
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