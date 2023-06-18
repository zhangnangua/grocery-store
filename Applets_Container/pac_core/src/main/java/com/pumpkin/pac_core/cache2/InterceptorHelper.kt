package com.pumpkin.pac_core.cache2

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.webkit.WebResourceRequest
import com.pumpkin.pac_core.download.WebDownloadCallback
import com.pumpkin.pac_core.download.WebDownloadHelper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.*

/**
 * howie
 * <scheme>://<authority><path>?<query>
 * [scheme:][//host:port][path][?query][#fragment]
 */
object InterceptorHelper {
    private const val CACHE_FILE_NAME = "pac"
    private const val FILE_SUFFIX = ".pac"

    fun destinationFolder(context: Context, url: String?): String? {
        val zipId = zipId(url) ?: return null
        return getRootPath(context) + File.separator + zipId

    }

    fun urlToPath(context: Context, url: String?): String? {
        val urlToPath = urlToPathInternal(url) ?: return null
        return destinationFolder(context, url) + File.separator + urlToPath + FILE_SUFFIX
    }

    private fun getRootPath(context: Context) =
        context.filesDir.toString() + File.separator + CACHE_FILE_NAME


    private fun urlToUri(url: String?): Uri? {

        if (url == null) {
            return null
        }

        return try {
            Uri.parse(url)
        } catch (e: Exception) {
            null
        }
    }

    private fun zipId(url: String?): String? {
        val nakedUlr = removeQueryAndFragment(url) ?: return null
        return MD5Utils.md5(nakedUlr)
    }

    private fun urlToPathInternal(url: String?): String? {
        val uri = urlToUri(url) ?: return null
        var removeQueryAndFragment = removeQueryAndFragment(uri)
        val scheme = uri.scheme
        if (!TextUtils.isEmpty(scheme)) {
            val schemePrefix = "$scheme://"
            removeQueryAndFragment = removeQueryAndFragment.replace(schemePrefix, "")
        }
        //if the last character is /,remove
        if (removeQueryAndFragment.endsWith("/")) {
            removeQueryAndFragment = removeQueryAndFragment.substring(0, removeQueryAndFragment.length - 1)
        }
        return removeQueryAndFragment
    }

    private fun removeQueryAndFragment(url: String?): String? {
        val uri = urlToUri(url) ?: return null
        return removeQueryAndFragment(uri)
    }

    private fun removeQueryAndFragment(uri: Uri): String {
        return uri.buildUpon().clearQuery().fragment(null).build().toString()
    }

}