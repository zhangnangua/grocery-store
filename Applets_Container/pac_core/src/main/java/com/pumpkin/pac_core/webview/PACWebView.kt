package com.pumpkin.pac_core.webview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView
import com.pumpkin.pac_core.bridge.JSBridge

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

        //设置缓存模式
        //LOAD_DEFAULT 根据HTTP协议header中设置的cache-control属性来执行加载策略
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.domStorageEnabled = true
//        settings.allowUniversalAccessFromFileURLs = true
//        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
//        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)


        // JS调用java统一入口，调用协议：window.pac.callPAC(string)
        addJavascriptInterface(JSBridge, "pac")
        // TODO: 2022/4/5 setWebChromeClient 以及 setWebViewClient  移植到外部处理，个性化较多

    }


}