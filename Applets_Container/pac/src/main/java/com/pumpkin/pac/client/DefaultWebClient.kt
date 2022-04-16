package com.pumpkin.pac.client

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.*

/**
 * 默认的WebClient实现
 *
 * @author pumpkin
 */
internal class DefaultWebClient : WebViewClient() {

    /**
     * 资源加载拦截
     */
    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        // TODO: 2022/4/5
        return super.shouldInterceptRequest(view, request)
    }

    /**
     * 资源加载拦截
     */
    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        // TODO: 2022/4/5
        return super.shouldInterceptRequest(view, url)
    }

    /**
     * 重定向
     */
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        // TODO: 2022/4/5
        return super.shouldOverrideUrlLoading(view, request)
    }

    /**
     * 重定向
     * android 7.0以下支持
     */
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        // TODO: 2022/4/5
        return super.shouldOverrideUrlLoading(view, url)
    }

    /**
     * 页面加载开始
     */
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        // TODO: 2022/4/5
        super.onPageStarted(view, url, favicon)
    }

    /**
     * 页面加载结束
     */
    override fun onPageFinished(view: WebView?, url: String?) {
        // TODO: 2022/4/5
        super.onPageFinished(view, url)
    }

    /**
     * 页面加载资源时报错，但对页面没有什么影响，例如favour.icon not found异常
     * android 5.0+ 支持
     */
    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        // TODO: 2022/4/5
        super.onReceivedHttpError(view, request, errorResponse)
    }

    /**
     * 页面报错
     * android6.0+支持
     */
    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        // TODO: 2022/4/5
        super.onReceivedError(view, errorCode, description, failingUrl)
    }

    /**
     * 页面报错
     */
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        // TODO: 2022/4/5
        super.onReceivedError(view, request, error)
    }

    /**
     *加载SSL证书异常，一般在请求https页面时捕获
     */
    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        // TODO: 2022/4/5
        super.onReceivedSslError(view, handler, error)
    }

    /**
     * 更新页面访问记录
     */
    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        // TODO: 2022/4/5
        super.doUpdateVisitedHistory(view, url, isReload)
    }

}