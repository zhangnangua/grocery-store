package com.pumpkin.data.http

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpClient {

    private const val MAX_DELAY_TIME = 20000L
    private const val TAG = "OkHttpClient"

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .dispatcher(Dispatcher().apply {
            maxRequestsPerHost = 10
            maxRequests = 128
        })
        .addInterceptor(HttpLoggingProxyInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }))
        .connectTimeout(MAX_DELAY_TIME, TimeUnit.SECONDS)
        .readTimeout(MAX_DELAY_TIME, TimeUnit.SECONDS)
        .build()

    fun getBuild(): Request.Builder {
        return Request.Builder()
    }

    fun bodyJson(json: String?): RequestBody {
        return json?.let {
            //创建requestBody 以json的形式
            val contentType: MediaType = "application/json; charset=utf-8".toMediaType()
            json.toRequestBody(contentType)
        } ?: run {
            //如果参数为null直接返回null
            FormBody.Builder().build()
        }
    }
}

fun Request.newCall(okHttpClient: OkHttpClient): Call = okHttpClient.newCall(this)