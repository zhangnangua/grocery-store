package com.pumpkin.webCache.util

import android.util.Log
import androidx.viewbinding.BuildConfig
import okio.*

/**
 * 处理流没有读完 不进行缓存的情况
 */
object CustomSource {

    const val TAG = "CustomSource"

    fun source(source: BufferedSource?, url: String) = source?.let {
        mySource(it, url)
    }

    private fun mySource(source: BufferedSource, url: String) = object : Source {
        override fun close() {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "close () -> url is $url")
            }
            source.close()
        }

        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = source.read(sink, byteCount)

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "read ( sink , byteCount ) -> url is $url , bytes is $bytesRead .")
            }

            return bytesRead
        }

        override fun timeout(): Timeout = source.timeout()
    }.buffer()
}