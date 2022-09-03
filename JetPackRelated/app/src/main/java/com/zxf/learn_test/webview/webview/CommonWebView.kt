package com.zxf.learn_test.webview.webview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView

@SuppressLint("SetJavaScriptEnabled")
class CommonWebView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    WebView(context, attrs, defStyleAttr) {
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
        settings.allowUniversalAccessFromFileURLs = true

        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.blockNetworkImage = false

        webChromeClient = CommonWebViewChromeClient()
        webViewClient = CommonWebViewClient()
    }
}