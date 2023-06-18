package com.pumpkin.pac_core.cache2

import android.text.TextUtils
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import com.pumpkin.pac_core.BuildConfig
import com.pumpkin.pac_core.cache.MimeTypeMapUtils
import com.pumpkin.pac_core.cache.NetUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.*

object RequestHelper {
    private const val TAG = "RequestHelper"

    fun request(request: WebResourceRequest, url: String, okHttpClient: OkHttpClient): Response {
        val builder = Request.Builder().url(url).also { requestBuilder ->
            request.requestHeaders?.forEach(action = { entry ->
                val key = entry.key
                val value = entry.value
                if (key != null && value != null) {
                    requestBuilder.addHeader(key, value)
                }
            })
        }
        //request
        return okHttpClient.newCall(builder.build()).execute()
    }

    fun saveFile(body: ResponseBody, targetFile: File) {
        val buf = ByteArray(1024 * 4)
        var fos: FileOutputStream? = null
        var srcInputStream: InputStream? = null
        try {
            val contentLength = body.contentLength()
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "current download length: $contentLength")
            }
//            var total = 0
            var len: Int
            srcInputStream = body.byteStream()
            val dir =
                targetFile.parentFile ?: throw FileNotFoundException("target file has no dir.")
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw FileNotFoundException("not found:" + dir.absolutePath)
                }
            }
            fos = FileOutputStream(targetFile)
            while (srcInputStream.read(buf).also { len = it } != -1) {
//                total += len
                fos.write(buf, 0, len)
//                val progress = (total * 100 / contentLength).toInt()
//                callback?.onProgress(total.toLong(), contentLength, progress)
            }
        } catch (e: FileNotFoundException) {
            throw e
        } catch (e: Exception) {
            throw e
        } finally {
            try {
                srcInputStream?.close()
                fos?.close()
            } catch (ignored: java.lang.Exception) {

            }
        }
    }

    fun resourceResponseByFile(
        request: WebResourceRequest,
        url: String,
        file: File
    ): WebResourceResponse? {
        if (file.exists()) {
            val mimeType: String = MimeTypeMapUtils.getMimeTypeFromUrl(url)
            try {
                val inputStream: InputStream = FileInputStream(file)
                val response = WebResourceResponse(mimeType, "", inputStream)
                val headers: MutableMap<String, String> = HashMap()
                request.requestHeaders?.forEach(action = { entry ->
                    val key = entry.key
                    val value = entry.value
                    if (key != null && value != null) {
                        headers[key] = value
                    }
                })
                // 解决webView跨域问题
                headers["Access-Control-Allow-Origin"] = "*"
                headers["Access-Control-Allow-Headers"] = "*"
                headers["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS, DELETE"
                headers["Access-Control-Allow-Credentials"] = "true"
                response.responseHeaders = headers
                return response
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        return null
    }


    fun resourceResponseByResponse(response: Response, url: String): WebResourceResponse? {
        val mimeType = MimeTypeMapUtils.getMimeTypeFromUrl(url)
        val webResourceResponse = WebResourceResponse(mimeType, "", response.body?.byteStream())
        var message = response.message
        if (TextUtils.isEmpty(message)) {
            message = "OK"
        }
        try {
            webResourceResponse.setStatusCodeAndReasonPhrase(response.code, message)
        } catch (e: Exception) {
            return null
        }
        webResourceResponse.responseHeaders =
            NetUtils.multimapToSingle(response.headers.toMultimap())
        return webResourceResponse
    }
}