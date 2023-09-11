package com.pumpkin.pac_core.webview

import android.graphics.Bitmap
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.pumpkin.pac_core.BuildConfig

/**
 * howie
 *
 */
internal class PACWebViewChrome : WebChromeClient() {

    var pageInterface: Webinterface.PageInterface? = null

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        pageInterface?.onReceivedTitle(view, title)
    }

    override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
        super.onReceivedIcon(view, icon)
        pageInterface?.onReceivedIcon(view, icon)
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        if (BuildConfig.DEBUG) {
            Log.d("PAC_WEB", "onConsoleMessage: ${consoleMessage?.message()}")
        }
        return super.onConsoleMessage(consoleMessage)
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        pageInterface?.onProgressChanged(view, newProgress)
    }

    fun clear() {
        pageInterface = null
    }
}