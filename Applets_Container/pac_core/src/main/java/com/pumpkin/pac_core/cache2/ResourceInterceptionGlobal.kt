package com.pumpkin.pac_core.cache2

import android.content.Context
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.pumpkin.pac_core.BuildConfig
import com.pumpkin.pac_core.cache.NetUtils
import com.pumpkin.pac_core.cache2.InterceptorHelper.CACHE_SIZE
import okhttp3.*
import java.util.concurrent.TimeUnit

/**
 * howie
 *
 * 资源拦截核心实现
 */
class ResourceInterceptionGlobal(val context: Context, private val rootUrl: String) {

    var cacheEnable = false

    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .dispatcher(Dispatcher().apply {
            maxRequestsPerHost = 10
            maxRequests = 128
        })
        .cache(Cache(InterceptorHelper.getCacheFile(context), CACHE_SIZE))
        .addNetworkInterceptor(HttpCacheInterceptor())
        .connectTimeout(MAX_DELAY_TIME, TimeUnit.SECONDS)
        .readTimeout(MAX_DELAY_TIME, TimeUnit.SECONDS)
        .eventListener(object : EventListener() {
            override fun cacheHit(call: Call, response: Response) {
                super.cacheHit(call, response)
                if (BuildConfig.DEBUG) {
                    Log.d(CACHE_TAG, "cacheHit: url is ${call.request().url}")
                }
            }

            override fun cacheConditionalHit(call: Call, cachedResponse: Response) {
                super.cacheConditionalHit(call, cachedResponse)
                if (BuildConfig.DEBUG) {
                    Log.d(CACHE_TAG, "cacheConditionalHit: url is ${call.request().url}")
                }
            }

            override fun cacheMiss(call: Call) {
                super.cacheMiss(call)
                if (BuildConfig.DEBUG) {
                    Log.d(CACHE_TAG, "cacheMiss: url is ${call.request().url}")
                }
            }
        })
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

    /**
     * 直接利用okhttp的缓存能力 进行缓存?
     */
    private fun internalIntercept(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        //just intercept GET
        if (request == null) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "request is null , method is ${request?.method} , cache enable is $cacheEnable")
            }
            return null
        }

        val extraHeaders = HashMap<String, String>()
        extraHeaders[HttpCacheInterceptor.CACHE_REQUEST_HEAD] = HttpCacheInterceptor.CACHE_FALSE
        val url = request.url.toString()

        if (canBlock(url)) {
            return RequestHelper.emptyWebResource()
        }

        if (request.method == "GET" && cacheEnable) {
            extraHeaders[HttpCacheInterceptor.CACHE_REQUEST_HEAD] = HttpCacheInterceptor.CACHE_TRUE
            //can cache
//            val fileExtensionFromUrl = MimeTypeMapUtils.getFileExtensionFromUrl(url)
//            if (TextUtils.isEmpty(fileExtensionFromUrl)) {
//                extraHeaders[HttpCacheInterceptor.CACHE_REQUEST_HEAD] =
//                    HttpCacheInterceptor.CACHE_FALSE
//            } else {
//                extraHeaders[HttpCacheInterceptor.CACHE_REQUEST_HEAD] =
//                    HttpCacheInterceptor.CACHE_TRUE
//            }
            //find cache resource
//            val filePath = InterceptorHelper.urlToPath(context, url, rootUrl)
//            if (filePath != null) {
//                val file = File(filePath)
//
//                return if (file.exists()) {
//                    if (BuildConfig.DEBUG) {
//                        Log.d(TAG, "internalIntercept: file exists $filePath . url is $url")
//                    }
//                    //intercept
//                    RequestHelper.resourceResponseByFile(request, url, file)
//                } else {
//                    if (BuildConfig.DEBUG) {
//                        Log.d(TAG, "internalIntercept: file not exists $filePath . url is $url")
//                    }
//                    //no find download
//                    val response = RequestHelper.request(request, url, okHttpClient)
//                    //save
//                    RequestHelper.saveFile(response.body!!, file)
//                    RequestHelper.resourceResponseByFile(request, url, file)
//                }
//            }
            //no cache
        }
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "real cache is ${extraHeaders[HttpCacheInterceptor.CACHE_REQUEST_HEAD]} , url is $url . method is ${request.method}")
        }
        val response = RequestHelper.request(request, url, okHttpClient, extraHeaders)
        if (response.code == 504 && !NetUtils.isConnected(context)) {
            return null
        }
        return RequestHelper.resourceResponseByResponse(response, url).also {
            if (it == null) {
                Log.d(CACHE_TAG, "resource is null , url is $url .")
            }
        }
    }

    private fun canBlock(url: String): Boolean {
        if (url.contains("googleads")
            || url.contains("googlesyndication")
            || url.contains("google-analytics")
            || url.contains("athena")
            || url.contains("hisavana")
            || url.contains("pay-japi.ahagamecenter")
        ) {
            return true
        }
        return false
    }

    companion object {
        private const val TAG = "interceptGlobal"
        private const val CACHE_TAG = "CACHE_TAG"
        private const val MAX_DELAY_TIME = 2000L
    }
}