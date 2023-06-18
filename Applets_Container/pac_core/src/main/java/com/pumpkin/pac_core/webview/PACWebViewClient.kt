package com.pumpkin.pac_core.webview

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.*

/**
 * howie
 *
 * client
 */
internal class PACWebViewClient : WebViewClient() {

    var pageInterface: Webinterface.PageInterface? = null
    var errorInterface: Webinterface.ErrorInterface? = null
    var resourceResponse: Webinterface.ResourceInterface? = null

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        return resourceResponse?.shouldInterceptRequest(view, request)
            ?: super.shouldInterceptRequest(view, request)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        errorInterface?.onReceivedError(view, request, error)
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
        errorInterface?.onReceivedHttpError(view, request, errorResponse)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        super.onReceivedSslError(view, handler, error)
        errorInterface?.onReceivedSslError(view, handler, error)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        pageInterface?.onPageFinished(view, url)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        pageInterface?.onPageStarted(view, url, favicon)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return resourceResponse?.shouldOverrideUrlLoading(view, request)
            ?: super.shouldOverrideUrlLoading(view, request)

    }

    fun clear() {
        pageInterface = null
        errorInterface = null
        resourceResponse = null
    }

}