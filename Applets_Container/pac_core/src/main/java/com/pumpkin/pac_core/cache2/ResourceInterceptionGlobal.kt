package com.pumpkin.pac_core.cache2

import android.content.Context
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.pumpkin.pac_core.cache.NetUtils
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * howie
 *
 * 资源拦截核心实现
 */
class ResourceInterceptionGlobal(val context: Context) {

    var cacheEnable = false

    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .dispatcher(Dispatcher().apply {

        })
        .callTimeout(MAX_DELAY_TIME, TimeUnit.SECONDS)
        .readTimeout(MAX_DELAY_TIME, TimeUnit.SECONDS)
        .hostnameVerifier { _, _ ->
            true
        }.build()


    fun intercept(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        return try {
            internalIntercept(view, request)
        } catch (e: Exception) {
            null
        }
    }

    private fun internalIntercept(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        //just intercept GET
        if (request?.method != "GET") {
            return null
        }

        val url = request.url.toString() ?: return null
        if (cacheEnable) {
            //can cache

            //find cache resource
            val filePath = InterceptorHelper.urlToPath(context, url)
            if (filePath != null) {
                val file = File(filePath)

                return if (file.exists()) {
                    //intercept
                    RequestHelper.resourceResponseByFile(request, url, file)
                } else {
                    //no find download
                    val response = RequestHelper.request(request, url, okHttpClient)
                    //save
                    RequestHelper.saveFile(response.body!!, file)
                    RequestHelper.resourceResponseByFile(request, url, file)
                }
            }
        }
        //no cache
        val response = RequestHelper.request(request, url, okHttpClient)
        if (response.code == 504 && !NetUtils.isConnected(context)) {
            return null
        }

        return RequestHelper.resourceResponseByResponse(response, url)
    }

    companion object {
        private const val MAX_DELAY_TIME = 2000L
    }
}