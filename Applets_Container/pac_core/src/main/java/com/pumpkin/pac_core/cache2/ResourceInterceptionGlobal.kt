package com.pumpkin.pac_core.cache2

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.pumpkin.pac_core.BuildConfig
import com.pumpkin.pac_core.cache.MimeTypeMapUtils
import com.pumpkin.pac_core.cache.NetUtils
import com.pumpkin.pac_core.cache2.InterceptorHelper.CACHE_SIZE
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
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
                Log.d(
                    TAG,
                    "request is null . method is ${request?.method}"
                )
            }
            return null
        }

        val extraHeaders = HashMap<String, String>()
        extraHeaders[HttpCacheInterceptor.CACHE_REQUEST_HEAD] = HttpCacheInterceptor.CACHE_FALSE
        val url = request.url.toString()
        if (request.method == "GET" && cacheEnable) {
            //can cache
//            val fileExtensionFromUrl = MimeTypeMapUtils.getFileExtensionFromUrl(url)
//            if (TextUtils.isEmpty(fileExtensionFromUrl)) {
//                extraHeaders[HttpCacheInterceptor.CACHE_REQUEST_HEAD] =
//                    HttpCacheInterceptor.CACHE_FALSE
//            } else {
//                extraHeaders[HttpCacheInterceptor.CACHE_REQUEST_HEAD] =
//                    HttpCacheInterceptor.CACHE_TRUE
//            }

            extraHeaders[HttpCacheInterceptor.CACHE_REQUEST_HEAD] = HttpCacheInterceptor.CACHE_TRUE

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
            if (BuildConfig.DEBUG) {
                Log.d(
                    TAG,
                    "real cache is ${extraHeaders[HttpCacheInterceptor.CACHE_REQUEST_HEAD]} , url is $url . method is ${request.method}"
                )
            }
            //no cache
            val response = RequestHelper.request(request, url, okHttpClient, extraHeaders)
            if (response.code == 504 && !NetUtils.isConnected(context)) {
                return null
            }

            return RequestHelper.resourceResponseByResponse(response, url)
        }
        if (BuildConfig.DEBUG) {
            Log.d(
                TAG,
                "real cache is ${extraHeaders[HttpCacheInterceptor.CACHE_REQUEST_HEAD]} , url is $url . method is ${request.method}"
            )
        }
        return null

    }

    companion object {
        private const val TAG = "interceptGlobal"
        private const val MAX_DELAY_TIME = 2000L
    }
}