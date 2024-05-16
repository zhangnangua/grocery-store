package com.pumpkin.webCache.requestHelper

import android.text.TextUtils
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import com.pumpkin.webCache.util.MimeTypeMapUtils
import okhttp3.*
import org.brotli.dec.BrotliInputStream
import java.io.*
import java.util.*

object RequestHelper {
    private const val TAG = "RequestHelper"
    private const val DEBUG = false

    fun request(request: WebResourceRequest, url: String, okHttpClient: OkHttpClient): Response {
        val okhttpRequest = Request.Builder().url(url).let { requestBuilder ->
            request.requestHeaders?.forEach(action = { entry ->
                val key = entry.key
                val value = entry.value
                if (key != null && value != null) {
                    requestBuilder.addHeader(key, value)
                }
            })
            val cookie = CookieManager.getInstance().getCookie(url)
            requestBuilder.removeHeader("cookie")
            requestBuilder.addHeader("cookie", cookie)
            //br 压缩支持
            requestBuilder.addHeader("accept-encoding", "gzip, deflate, br")
            requestBuilder.addHeader("sec-fetch-mode", "no-cors")
            requestBuilder.addHeader("sec-fetch-site", "same-site")

            requestBuilder.build()
        }

        if (DEBUG) {
            Log.d(TAG, "request () -> header is ${okhttpRequest.headers} , url is ${request.url}")
        }
        //request
        return okHttpClient.newCall(okhttpRequest).execute()
    }

    fun resourceResponseByResponse(response: Response, url: String): WebResourceResponse? {
        val mimeType = MimeTypeMapUtils.getMimeTypeFromUrl(url, response) ?: return null

        //br 压缩支持
        val brStream = response.header("Content-Encoding")?.let {
            if (it != "br" || response.body == null) {
                return null
            }
            BrotliInputStream(response.body!!.source().inputStream())
        }

        val webResourceResponse = WebResourceResponse(mimeType, "utf-8", brStream
            ?: response.body?.byteStream())
        var message = response.message
        if (TextUtils.isEmpty(message)) {
            message = "OK"
        }
        try {
            webResourceResponse.setStatusCodeAndReasonPhrase(response.code, message)
        } catch (e: Exception) {
            return null
        }

        // headers
        if (DEBUG) {
            val startTime = System.currentTimeMillis()
            val headersToMap = headersToMap(response.headers)
            webResourceResponse.responseHeaders = headersToMap
            val endTime = System.currentTimeMillis()
            Log.d(TAG, """
                code is ${response.code}
                NetUtils.multimapToSingle(response.headers.toMultimap()): 
                execute time is ${endTime - startTime} ， 
                headers is $headersToMap
                mimeType is $mimeType
            """.trimIndent())
        } else {
            webResourceResponse.responseHeaders = headersToMap(response.headers).apply {
                if (brStream != null) {
                    put("Content-Encoding", "br")
                }
            }
        }
        return webResourceResponse
    }

    private fun headersToMap(headers: Headers): HashMap<String, String> {
        val result = HashMap<String, String>()
        for (i in 0 until headers.size) {
            val name = headers.name(i).lowercase(Locale.US)
            val lastValue = result[name]
            var value = headers.value(i)
            if (lastValue != null) {
                value = "$lastValue;$value"
            }
            result[name] = value
        }
        return result
    }


    fun saveFile(body: ResponseBody, targetFile: File) {
        val buf = ByteArray(1024 * 4)
        var fos: FileOutputStream? = null
        var srcInputStream: InputStream? = null
        try {
//            val contentLength = body.contentLength()
//            if (BuildConfig.DEBUG) {
//                Log.d(TAG, "current download length: $contentLength")
//            }
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
        url: String,
        file: File
    ): WebResourceResponse? {
        if (file.exists()) {
            val mimeType: String? = MimeTypeMapUtils.getMimeTypeFromUrl(url)
            try {
                val inputStream: InputStream = FileInputStream(file)
                val response = WebResourceResponse(mimeType, "UTF_8", inputStream)
                val headers: MutableMap<String, String> = HashMap()
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

}