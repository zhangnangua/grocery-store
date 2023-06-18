package com.pumpkin.pac_core.webview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.AttributeSet
import android.webkit.*

/**
 * 小程序容器
 *
 * @author pumpkin
 */
@SuppressLint("SetJavaScriptEnabled")
class PACWebView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    WebView(context, attrs, defStyle) {

    private val pacWebViewChrome: PACWebViewChrome
    private val pacWebViewClient: PACWebViewClient

    init {
        //设置支持JS
        settings.javaScriptEnabled = true
        //设置是否支持meta标签来控制缩放
        settings.useWideViewPort = true
        //缩放至屏幕的大小
        settings.loadWithOverviewMode = true
        //设置内置的缩放控件（若SupportZoom为false，该设置项无效）,若关闭则无法进行缩放
        settings.builtInZoomControls = false

        settings.allowContentAccess = true
        settings.allowFileAccess = true
//        settings.allowUniversalAccessFromFileURLs = true
//        settings.allowFileAccessFromFileURLs = true

        settings.setAppCacheEnabled(true)
        settings.setAppCachePath(context.cacheDir.absolutePath)
        settings.databaseEnabled = true

        settings.userAgentString = "${settings.userAgentString}PAC"

        //设置缓存模式
        //LOAD_DEFAULT 根据HTTP协议header中设置的cache-control属性来执行加载策略
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.domStorageEnabled = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

        pacWebViewChrome = PACWebViewChrome()
        pacWebViewClient = PACWebViewClient()
    }

    fun clear() {
        pacWebViewChrome.clear()
        pacWebViewClient.clear()
    }

    fun setPageInterface(pageInterface: Webinterface.PageInterface?) {
        pacWebViewChrome.pageInterface = pageInterface
        pacWebViewClient.pageInterface = pageInterface
    }

    fun setErrorInterface(errorInterface: Webinterface.ErrorInterface?) {
        pacWebViewClient.errorInterface = errorInterface
    }

    fun setResourceInterface(resourceInterface: Webinterface.ResourceInterface?) {
        pacWebViewClient.resourceResponse = resourceInterface
    }


}


interface Webinterface {
    interface PageInterface {
        fun onPageFinished(view: WebView?, url: String?)
        fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?)
        fun onProgressChanged(view: WebView?, newProgress: Int)
        fun onReceivedIcon(view: WebView?, icon: Bitmap?)
        fun onReceivedTitle(view: WebView?, title: String?)
    }

    interface ErrorInterface {
        fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        )

        fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        )

        fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?)
    }

    interface ResourceInterface {

        fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse?

        fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean
    }

}