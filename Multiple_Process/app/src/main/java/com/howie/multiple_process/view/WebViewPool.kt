package com.howie.multiple_process.view

import android.content.Context
import android.content.MutableContextWrapper
import android.os.Looper
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewPool {

    private val webViewCache: MutableList<WebView> = ArrayList(NUM)

    fun prepare(context: Context) {
        if (webViewCache.isEmpty()) {
            Looper.myQueue().addIdleHandler {
                webViewCache.add(create(MutableContextWrapper(context)))
                false
            }
        }
    }

    fun get(context: Context): WebView {
        if (webViewCache.isEmpty()) {
            webViewCache.add(create(MutableContextWrapper(context)))
        }
        return webViewCache.removeFirst().apply {
            val contextWrapper = context as MutableContextWrapper
            contextWrapper.baseContext = context
            clearHistory()
            resumeTimers()
        }
    }

    fun recycle(webView: WebView) {
        try {
            with(webView){
                stopLoading()
                loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
                clearHistory()
                pauseTimers()
                webChromeClient = null
                webViewClient = WebViewClient()
            }
            val parent = webView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(webView)
            }
        } catch (e: Exception) {
        } finally {
            if (!webViewCache.contains(webView)) {
                webViewCache.add(webView)
            }
        }
    }

    fun destroy() {
        try {
            webViewCache.removeAll {
                it.removeAllViews()
                it.destroy()
                false
            }
        } catch (e: Exception) {
        }
    }

    private fun create(context: Context) = WebView(context)

    companion object{
        const val NUM = 1
    }
}