package com.pumpkin.parse

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * 禁止图片load
 */
class ParseEngine @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    WebView(context, attrs, defStyle) {

    init {
        settings.javaScriptEnabled = true
        settings.blockNetworkImage = true

        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                evaluateJavascript("""
            function fetch() {
            const result = []
            const gameItems = document.querySelectorAll('.categorized-grid__game-item');
            gameItems.forEach((gameItem, index) => {
                const anchorElement = gameItem.querySelector('a');
                const spanElement = gameItem.querySelector('.game-card__description span');
                const imgElement = gameItem.querySelector('img');
                const hrefAttribute = anchorElement.getAttribute('href');
                const spanText = spanElement.textContent;
                const imgSrc = imgElement.getAttribute('src');
                result.push({
                    'href': hrefAttribute,
                    'spanText': spanText,
                    'imgSrc': imgSrc
                })
            });
            return result;
        }
        fetch();
        """.trimIndent(), ValueCallback {
                    Log.d(TAG, "return : $it")
                })
            }
        }
    }

    companion object {
        private const val TAG = "ParseEngine"
    }

}

