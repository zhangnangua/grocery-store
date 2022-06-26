package com.howie.multiple_process

import com.howie.multiple_process.helper.AppUtil
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import kotlin.coroutines.resumeWithException

//suspend fun writeString(filePath: String, content: String) =
//    withContext(Dispatchers.IO) {
//
//    }

suspend fun writeString(
    filePath: String = "${AppUtil.application.cacheDir}/process_test/test.txt",
    content: String
) = suspendCancellableCoroutine<Boolean> {
    var going = true
    it.invokeOnCancellation {
        // cancel
        going = false
    }
    Thread {
        if (it.isActive) {
            //创建文件
            val file = File(filePath)
            val parentFile = file.parentFile
            if (parentFile == null) {
                it.resumeWithException(IllegalAccessException("path error."))
                return@Thread
            }
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            if (!file.exists()) {
                file.createNewFile()
            }

            while (going && it.isActive) {
                var filerWriter: FileWriter? = null
                var bufWriter: BufferedWriter? = null
                try {
                    // true 进行写覆盖
                    filerWriter = FileWriter(file, true)
                    bufWriter = BufferedWriter(filerWriter)
                    bufWriter.write(content)
                    bufWriter.newLine()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    bufWriter?.close()
                    filerWriter?.close()
                }
            }
            it.resumeWith(Result.success(true))
        } else {
            it.resumeWithException(CancellationException())
        }
    }.start()
}