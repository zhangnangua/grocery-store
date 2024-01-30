package com.pumpkin.webCache.interceptors

import android.app.Application
import android.util.Log
import android.webkit.WebResourceResponse
import androidx.viewbinding.BuildConfig
import com.pumpkin.webCache.base.DefaultInterceptor
import com.pumpkin.webCache.interceptor.Chain
import com.pumpkin.webCache.requestHelper.CacheHelper
import com.pumpkin.webCache.requestHelper.HttpCacheInterceptor
import com.pumpkin.webCache.requestHelper.InterceptorConfig
import com.pumpkin.webCache.requestHelper.RequestHelper
import okhttp3.*
import java.util.concurrent.TimeUnit

class DynamicInterceptor(application: Application, c: InterceptorConfig) : DefaultInterceptor() {

    private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .dispatcher(Dispatcher().apply {
            maxRequestsPerHost = 10
            maxRequests = 128
        })
        .cache(Cache(CacheHelper.getDynamicCacheFile(application), CacheHelper.DYNAMIC_CACHE_SIZE))
        .addNetworkInterceptor(HttpCacheInterceptor(c))
        .connectTimeout(MAX_DELAY_TIME, TimeUnit.SECONDS)
        .readTimeout(MAX_DELAY_TIME, TimeUnit.SECONDS)
        .eventListener(object : EventListener() {
            override fun cacheHit(call: Call, response: Response) {
                super.cacheHit(call, response)
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "cacheHit: url is ${call.request().url}")
                }
            }

            override fun cacheConditionalHit(call: Call, cachedResponse: Response) {
                super.cacheConditionalHit(call, cachedResponse)
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "cacheConditionalHit: url is ${call.request().url}")
                }
            }

            override fun cacheMiss(call: Call) {
                super.cacheMiss(call)
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "cacheMiss: url is ${call.request().url}")
                }
            }
        })
        .hostnameVerifier { _, _ ->
            true
        }.build()

    override fun findResource(chain: Chain): WebResourceResponse? {
        val response = try {
            RequestHelper.request(chain.request, chain.url, okHttpClient)
        } catch (e: Exception) {
            null
        }
        response ?: return null
        if (response.code == 504) {
            return null
        }
        return RequestHelper.resourceResponseByResponse(response, chain.url).also {
            if (BuildConfig.DEBUG && it == null) {
                Log.d(TAG, "resource is null , url is ${chain.url} .")
            }
        }
    }

    companion object {
        private const val MAX_DELAY_TIME = 20000L
        private const val TAG = "DI"
    }
}