package com.pumpkin.pac_core.cache

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView


/**
 * pumpkin
 *
 * WebView请求拦截处理
 */
internal interface WebViewRequestInterceptor {

    /**
     * 请求拦截
     */
    fun interceptRequest(request: WebResourceRequest?): WebResourceResponse?
    fun interceptRequest(url: String?): WebResourceResponse?

    /**
     * loadUrl sdk<21时 需要通过它手动设置 相关请求头 Origin Referer UserAgent
     * Origin 用于指明当前请求来自于哪个站点 格式：https://baidu.com
     * Referer 告诉服务器我是从哪个页面链接过来的 格式：一个完成的url
     * UserAgent
     */
    fun loadUrl(webView: WebView, url: String)

    /**
     * 开始强制缓存
     * true 开启缓存
     * false webView正常资源加载逻辑
     */
    fun enableCache(enable: Boolean)
}