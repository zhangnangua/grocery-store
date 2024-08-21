package com.pumpkin.pac_core.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.AttributeSet
import android.webkit.CookieManager
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import com.pumpkin.pac_core.BuildConfig

/**
 * 小程序容器
 *
 * todo 使用次数回收 增加
 *
 * @author pumpkin
 */
@SuppressLint("SetJavaScriptEnabled")
class PACWebEngine
@JvmOverloads
constructor(context: Context,
            attrs: AttributeSet? = null,
            defStyle: Int =
                try {
                    Resources.getSystem().getIdentifier("webViewStyle", "attr", "android")
                } catch (e: Exception) {
                    0
                }
) : WebView(context, attrs, defStyle) {

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

//        settings.setAppCacheEnabled(true)
//        settings.setAppCachePath(context.cacheDir.absolutePath)
        settings.databaseEnabled = true

        settings.userAgentString = "${WebSettings.getDefaultUserAgent(context)}PAC"

        settings.setSupportMultipleWindows(true)

        setWebContentsDebuggingEnabled(BuildConfig.DEBUG)

        //设置缓存模式
        //LOAD_DEFAULT 根据HTTP协议header中设置的cache-control属性来执行加载策略
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.domStorageEnabled = true
        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

        pacWebViewChrome = PACWebViewChrome()
        pacWebViewClient = PACWebViewClient()
    }

    fun addClient() {
        webViewClient = pacWebViewClient
        webChromeClient = pacWebViewChrome
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
        fun onLoadResource(view: WebView?, url: String?)
        fun doUpdateVisitedHistory(view: WebView?, url: String?, reload: Boolean)
        fun onPageCommitVisible(view: WebView?, url: String?)
        fun onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean?
        fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean?
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


    open class DefaultPage : PageInterface {
        override fun onPageFinished(view: WebView?, url: String?) {
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
        }

        override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
        }

        override fun onLoadResource(view: WebView?, url: String?) {

        }

        override fun doUpdateVisitedHistory(view: WebView?, url: String?, reload: Boolean) {

        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {

        }

        override fun onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean? {
            return false
        }

        override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean? {
            return false
        }
    }

    open class DefaultError : ErrorInterface {
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {

        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {

        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {

        }

    }

}

internal val xRequestHeader = mapOf(
    // For every request WebView sends a "X-requested-with" header with the package name of the
    // application. We can't really prevent that but we can at least send an empty value.
    // Unfortunately the additional headers will not be propagated to subsequent requests
    // (e.g. redirects). See issue #696.
    "X-Requested-With" to "",
)