package com.pumpkin.data.http

import android.util.Log
import com.pumpkin.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HttpLoggingProxyInterceptor(private val loggingInterceptor: Interceptor) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (BuildConfig.DEBUG || Log.isLoggable("OkHttp", Log.DEBUG)) {
            loggingInterceptor.intercept(chain)
        } else {
            chain.proceed(chain.request())
        }
    }

}