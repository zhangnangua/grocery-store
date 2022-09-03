package com.zxf.learn_test.webview.webview

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import com.zxf.utils.AppUtil

class CommonWebViewChromeClient : WebChromeClient() {

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        if (AppUtil.isDebug) {
            Log.d(TAG, "onConsoleMessage: ${consoleMessage?.message()}")
        }
        return super.onConsoleMessage(consoleMessage)
    }

    companion object {
        const val TAG = "WebClient"
    }
}