package com.pumpkin.okhttp

import com.pumpkin.okhttp.util.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.threadFactory
import okio.Buffer
import okio.BufferedSink
import okio.source
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

//自定义dispatcher 定义线程池
val dispatcher = Dispatcher(
    ThreadPoolExecutor(
        0,
        Int.MAX_VALUE,
        0,
        TimeUnit.SECONDS,
        SynchronousQueue(),
        threadFactory(
            "${
                OkHttpClient::class.java.name.removePrefix("okhttp3.").removeSuffix("Client")
            } Dispatcher", false
        )
    )
).apply {
    //设置最大请求数
    maxRequests = 64
    //最多同时请求的主机
    maxRequestsPerHost = 5
}

//region 其他类常用封装

/**
 * 进度通用回调  不使用flow封装的话 使用这个
 */
internal typealias ProgressBlock = (state: DownloadState) -> Unit

/**
 * 上传状态机
 */
sealed class UploadState {
    /**
     * 未开始
     */
    object UnStart : UploadState()

    /**
     * 文件不存在
     */
    object FileNotExist : UploadState()

    /**
     * 上传完成
     */
    object Complete : UploadState()

    /**
     * 上传中
     */
    class Progress(var totalNum: Long, var current: Long) : UploadState()

    /**
     * 失败
     */
    class Error(val e: Exception) : UploadState()
}

/**
 * 下载状态机
 */
sealed class DownloadState {

    /**
     * 未开始
     */
    object UnStart : DownloadState()

    /**
     * 下载中
     */
    class Progress(var totalNum: Long, var current: Long) : DownloadState()

    /**
     * 下载完成
     */
    class Complete(val file: File?) : DownloadState()

    /**
     * 下载失败
     */
    class Failure(val e: Throwable?) : DownloadState()

    /**
     * 下载失败
     */
    class FileExistsNoDownload(val file: File?) : DownloadState()

}

//endregion

/**
 * token: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
 */
fun main() {
//region  get请求
    //简单请求
//    simpleGetUse("https://api.github.com/users/zhangnangua")
    //仓库信息 公开
//    simpleGetUse("https://api.github.com/repos/zhangnangua/grocery-store/repos")

    //增加token请求头
    val authString = "zhangnangua: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"
    val bytesBase64 = Base64.getEncoder().encode(authString.encodeToByteArray())
    val realRequestAuth = String(bytesBase64).also { log(it) }

//    simpleGetUse("https://api.github.com/users/zhangnangua", mapOf("Authorization" to "Basic $realRequestAuth"))
    //获取配置信息 使用请求头
//    simpleGetUse("https://api.github.com/user", mapOf("Authorization" to "Basic $realRequestAuth"))

    //总体仓库信息预览  总体预览
//    simpleGetUse(
//        "https://api.github.com/user/repos",
//        mapOf("Authorization" to "token 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")
//    )

    //查看所有的问题issues
//    simpleGetUse(
//        "https://api.github.com/issues",
//        mapOf("Authorization" to "token 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")
//    )

//    simpleGetUse(
//        "https://api.github.com/repos/rails/rails/issues"
//    )
//    simpleGetUse(
//        "https://api.github.com/repositories/8514/issues?page=2"
//    )

    //一般下载测试
//    downloadFile(
//        "http://haoshangtong2518-1300600680.cos.ap-beijing.myqcloud.com/20220108221937982.mp4",
//        "download/20220108221937982.mp4"
//    ) { state: DownloadState ->
//        when (val s = state) {
//            is DownloadState.Complete -> log("下载完成 文件路径为 ${s.file?.absoluteFile}")
//            is DownloadState.Failure -> log("下载失败  ${s.e?.message}")
//            is DownloadState.FileExistsNoDownload -> log("已经存在  ${s.file?.absoluteFile}")
//            is DownloadState.Progress -> log("下载中  ${(s.current.toFloat() / s.totalNum) * 100}%  current:${s.current.toFloat()}  totalNum:${s.totalNum}")
//            DownloadState.UnStart -> log("下载未开始")
//        }
//    }

    //callbackFlow 封装测试   Dispatchers.IO 模拟主线程
    runBlocking(Dispatchers.IO) {
        downloadFileUseFlow(
            "http://haoshangtong2518-1300600680.cos.ap-beijing.myqcloud.com/20220108221937982.mp4",
            "download/20220108221937982.mp4"
        ).onEach { downloadState ->
            when (downloadState) {
                is DownloadState.Complete -> {
                    log("下载完成 文件路径为 ${downloadState.file?.absoluteFile}  文件尺寸 ${downloadState.file?.length()}")
                }
                is DownloadState.Failure -> log("下载失败  ${downloadState.e?.message}")
                is DownloadState.FileExistsNoDownload -> log("已经存在  ${downloadState.file?.absoluteFile}")
                is DownloadState.Progress -> log("下载中  ${(downloadState.current.toFloat() / downloadState.totalNum) * 100}%  总字节 ${downloadState.totalNum}")
                DownloadState.UnStart -> log("下载未开始")
            }
        }.launchIn(this)
    }

//endregion


//region  post请求
    //创造一个仓库  create a repository  post体需要以json格式传递
//    simplePostUseJson(
//        "https://api.github.com/user/repos",
//        Gson().toJson(
//            mapOf(
//                "name" to "blog",
//                "auto_init" to "true",
//                "private" to "true",
//                "gitignore_template" to "nanoc"
//            )
//        ),
//        mapOf(
//            "Content-Type" to "application/json",
//            "Authorization" to "token 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"
//        )
//    )

//    create a issue
//    simplePostUseJson(
//        "https://api.github.com/repos/zhangnangua/learning-summary/issues",
//        Gson().toJson(
//            mapOf(
//                "title" to "New logo",
//                "body" to "We should have one",
//                "repo" to "learning-summary"
//            )
//        ), mapOf(
//            "Authorization" to "token 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"
//        )
//    )

    //一般上传文件  不带进度条
//    simpleUploadFile(
//        "https://api.github.com/markdown/raw",
//        "settings.gradle",
//        "multipart/form-data; charset=utf-8".toMediaType()
//    ) { s ->
//        when (s) {
//            UploadState.Complete -> {
//                log("上传完成")
//            }
//            UploadState.FileNotExist -> {
//                log("上传失败，文件不存在")
//            }
//            is UploadState.Progress -> {
//                log("上传中  ${(s.current.toFloat() / s.totalNum) * 100}%")
//            }
//            UploadState.UnStart -> {
//                log("上传未开始")
//            }
//            is UploadState.Error -> {
//                log("上传失败  ${s.e.message}")
//            }
//        }
//    }

    //文件和其他表单参数一块上传
//    multipartUpload(
//        "https://api.github.com/markdown/raw",
//        "settings.gradle",
//        "multipart/form-data; charset=utf-8".toMediaType(),
//        mapOf(
//            "name" to "blog",
//            "auto_init" to "true",
//            "private" to "true",
//            "gitignore_template" to "nanoc"
//        )
//    ) { s ->
//        when (s) {
//            UploadState.Complete -> {
//                log("上传完成")
//            }
//            UploadState.FileNotExist -> {
//                log("上传失败，文件不存在")
//            }
//            is UploadState.Progress -> {
//                log("上传中  ${(s.current.toFloat() / s.totalNum) * 100}%")
//            }
//            UploadState.UnStart -> {
//                log("上传未开始")
//            }
//            is UploadState.Error -> {
//                log("上传失败  ${s.e.message}")
//            }
//        }
//    }

    //上传文件带进度条
//    multipartUploadProgress(
////        "https://api.github.com/markdown/raw",
//        "https://baidu.com",
//        "download/WeChatSetup.exe",
//        "application/octet-stream".toMediaType(),
//        mapOf(
//            "name" to "blog",
//            "auto_init" to "true",
//            "private" to "true",
//            "gitignore_template" to "nanoc"
//        )
//    ) { s ->
//        when (s) {
//            UploadState.Complete -> {
//                log("上传完成")
//            }
//            UploadState.FileNotExist -> {
//                log("上传失败，文件不存在")
//            }
//            is UploadState.Progress -> {
//                log("上传中  ${(s.current.toFloat() / s.totalNum) * 100}%")
//            }
//            UploadState.UnStart -> {
//                log("上传未开始")
//            }
//            is UploadState.Error -> {
//                log("上传失败  ${s.e.message}")
//            }
//        }
//    }

//endregion
}

//region 简单GET请求方法

/**
 * get简单的
 */
fun simpleGetUse(url: String, headers: Map<String, String>? = null) {
    //设置最大尺寸为1MB缓存
//    val cache = Cache(File("cache"), 1024 * 1024 * 8)
//    val okHttpClient = OkHttpClient.Builder().cache(cache).build()

    val okHttpClient = OkHttpClient()
    val requestBuilder = Request.Builder().url(url)
    headers?.onEach { (name, value) ->
        requestBuilder.addHeader(name, value)
    }
    okHttpClient.newCall(requestBuilder.build()).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            log("go failure ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val msg = if (response.isSuccessful) {
                simpleDealData(response)
            } else {
                "failure ${response.message}"
            }
            log(msg)
        }
    })
}

//endregion

//region post简单使用
/**
 * post简单使用
 */
fun simplePostUseFrom(url: String, params: Map<String, String>? = null, headers: Map<String, String>? = null) {

    /**
     *
     * 四个超时时间区别
     *
     * callTimeout ： 调用超时跨越整个调用：解析 DNS、连接、写入请求正文、服务器处理和读取响应正文。 如果调用需要重定向或重试，则所有调用都必须在一个超时期限内完成。
     *
     * 下面三个直接设置给了对应的socket，分别是tcp连接超时，读超时，写超时
     * connectTimeout 将 TCP 套接字连接到目标主机时应用连接超时。 默认值为 10 秒。
     * readTimeout 读取超时适用于 TCP 套接字和单个读取 IO 操作，包括在 [响应] 的 [源] 上。 默认值为 10 秒。
     * writeTimeout 写入超时适用于单个写入 IO 操作。 默认值为 10 秒。
     */
    val formBody = FormBody.Builder()
        .also { builder ->
            params?.forEach { (name, value) ->
                builder.add(name, value)
            }
        }.build()
    postCommonRequest(url, formBody, headers)
}

/**
 * post简单使用  JSON 格式
 */
fun simplePostUseJson(url: String, jsonStr: String? = null, headers: Map<String, String>? = null) {

    /**
     *
     * 四个超时时间区别
     *
     * callTimeout ： 调用超时跨越整个调用：解析 DNS、连接、写入请求正文、服务器处理和读取响应正文。 如果调用需要重定向或重试，则所有调用都必须在一个超时期限内完成。
     *
     * 下面三个直接设置给了对应的socket，分别是tcp连接超时，读超时，写超时
     * connectTimeout 将 TCP 套接字连接到目标主机时应用连接超时。 默认值为 10 秒。
     * readTimeout 读取超时适用于 TCP 套接字和单个读取 IO 操作，包括在 [响应] 的 [源] 上。 默认值为 10 秒。
     * writeTimeout 写入超时适用于单个写入 IO 操作。 默认值为 10 秒。
     */
    val requestBody = jsonStr?.let {
        val contentType: MediaType = "application/json; charset=utf-8".toMediaType()
        jsonStr.toRequestBody(contentType)
    } ?: run {
        FormBody.Builder().build()
    }
    postCommonRequest(url, requestBody, headers)

}

//endregion

//region 下载封装

// TODO: 2021/12/27 需要改造为Flow类型
/**
 * 文件下载
 */
fun downloadFile(url: String, destFileDirName: String, progressBlock: ProgressBlock) {
    //下载状态  默认未开始
    var state: DownloadState = DownloadState.UnStart
    progressBlock(state)

    // TODO: 2021/12/27 file 创建与判断可以封装
    /**
     * file 创建判断  可以封装
     */
    val file = File(destFileDirName)
    val parentFile = file.parentFile
    if (!parentFile.exists()) {
        parentFile.mkdirs()
    }
    if (file.exists()) {
        //文件存在 不需要下载
        state = DownloadState.FileExistsNoDownload(file)
        progressBlock(state)
        return
    } else {
        file.createNewFile()
    }

    //下载
    val okHttpClient = OkHttpClient()
    val request = Request.Builder().url(url).build()
    okHttpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            state = DownloadState.Failure(e)
            progressBlock(state)
        }

        override fun onResponse(call: Call, response: Response) {
            response.use { res ->
                //完整长度
                var totalLength = 0L
                //写入字节
                val bytes = ByteArray(2048)
                val fileOutputStream = FileOutputStream(file)

                res.body?.also { responseBody ->
                    totalLength = responseBody.contentLength()
                }?.byteStream()?.let { inputStream ->
                    try {
                        var currentProgress = 0L
                        var len = 0
                        do {
                            if (len != 0) {
                                currentProgress += len
                                fileOutputStream.write(bytes, 0, len)
                            }
                            //状态改变
                            state = DownloadState.Progress(totalLength, currentProgress)
                            progressBlock(state)
                            len = inputStream.read(bytes, 0, bytes.size)
                        } while (len != -1)
                        //状态改变完成
                        state = DownloadState.Complete(file)
                        progressBlock(state)
                    } catch (e: Exception) {
                        state = DownloadState.Failure(e)
                        progressBlock(state)
                    } finally {
                        inputStream.close()
                        fileOutputStream.close()
                    }
                }
            }
        }

    })

}

/**
 * 使用Flow改造文件下载
 * callbackFlow  可以保证线程的安全  底层是channel
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun downloadFileUseFlow(url: String, destFileDirName: String) = callbackFlow<DownloadState> {
    var state: DownloadState = DownloadState.UnStart
    send(state)

    //获取文件对象
    val file = File(destFileDirName).also { file ->
        val parentFile = file.parentFile
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        if (file.exists()) {
            state = DownloadState.FileExistsNoDownload(file)
            send(state)
            //流关闭，返回
            close()
            return@callbackFlow
        } else {
            file.createNewFile()
        }
    }
    //下载
    val okHttpClient = OkHttpClient().newBuilder()
        .dispatcher(dispatcher)
        .writeTimeout(30, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.MINUTES)
        .callTimeout(30, TimeUnit.MINUTES)
        .build()
    val request = Request.Builder()
        .url(url)
        .build()
    okHttpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            //更新状态
            state = DownloadState.Failure(e)
            this@callbackFlow.trySendBlocking(state)
            close()
        }

        override fun onResponse(call: Call, response: Response) {
            //下载
            val body = response.body
            if (response.isSuccessful && body != null) {
                //完整长度
                val totalNum: Long = body.contentLength()
                //当前下载的长度
                var currentProgress: Long = 0L
                var len = 0

                response.use {
                    //等效于   FileOutputStream(file)  输出流
                    val outputStream = file.outputStream()
                    //输入流
                    val byteStream = body.byteStream()
                    try {
                        val bates = ByteArray(2048)

                        //设置状态对象拉出来，避免循环一直创建对象
                        state = DownloadState.Progress(totalNum, currentProgress)
                        //循环读写
                        do {
                            if (len != 0) {
                                currentProgress += len
                                outputStream.write(bates, 0, len)
                            }
                            //更新进度
                            (state as DownloadState.Progress).current = currentProgress
                            this@callbackFlow.trySendBlocking(state)
                            len = byteStream.read(bates, 0, bates.size)
                        } while (len != -1)
                        //下载完成
                        state = DownloadState.Complete(file)
                        this@callbackFlow.trySendBlocking(state)
                    } catch (e: Exception) {
                        state = DownloadState.Failure(e)
                        this@callbackFlow.trySendBlocking(state)
                    } finally {
                        outputStream.close()
                        byteStream.close()
                        //关闭callbackFlow
                        this@callbackFlow.close()
                    }
                }

            } else {
                //更新状态且关闭
                state = DownloadState.Failure(Exception(response.message))
                this@callbackFlow.trySendBlocking(state)
                close()
            }
        }

    })
    //使用channelFlow 必须使用awaitClose 挂起flow等待channel结束
    awaitClose {
        log("callbackFlow关闭 .")
    }
}
    .buffer(Channel.CONFLATED) //设置 立即使用最新值 buffer里面会调用到fuse函数，继而调用到create函数重新创建channelFlow
    .flowOn(Dispatchers.Default) //直接设置callbackFlow执行在异步线程
    .catch { e ->
        //异常捕获重新发射
        emit(DownloadState.Failure(e))
    }

/**
 * 单纯的文件上传
 */
inline fun simpleUploadFile(
    url: String,
    filePath: String,
    contentType: MediaType? = null,
    crossinline block: (UploadState) -> Unit
) {
    var state: UploadState = UploadState.UnStart
    block(state)

    val file = File(filePath)
    if (!file.exists()) {
        //文件不存在则不上传
        state = UploadState.FileNotExist
        block(state)
        return
    }

    val request = Request.Builder()
        .url(url)
        .post(file.asRequestBody(contentType))
        .build()

    val client = OkHttpClient.Builder()
        .dispatcher(dispatcher)
        .writeTimeout(30, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.MINUTES)
        .connectTimeout(70, TimeUnit.SECONDS)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            log(e.message)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                state = UploadState.Complete
                block(state)
            } else {
                log("请求失败")
            }
        }

    })

}

/**
 * 文件上传，表单和文件一起提交
 */
inline fun multipartUpload(
    url: String,
    filePath: String,
    contentType: MediaType? = null,
    params: Map<String, String>? = null,
    crossinline block: (UploadState) -> Unit
) {
    var state: UploadState = UploadState.UnStart
    block(state)

    val file = File(filePath)

    val body = MultipartBody.Builder()
        .also {
            params?.forEach { (k, v) ->
                it.addFormDataPart(k, v)
            }
        }.also {
            if (file.exists()) {
                it.addFormDataPart("filename", file.name, file.asRequestBody(contentType))
            }
        }.build()

    val request = Request.Builder()
        .url(url)
        .post(body)
//        .addHeader() 可以增加请求头
        .build()

    val client = OkHttpClient.Builder()
        .dispatcher(dispatcher)
        .writeTimeout(30, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.MINUTES)
        .connectTimeout(70, TimeUnit.SECONDS)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            log(e.message)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                state = UploadState.Complete
                block(state)
            } else {
                log("请求失败")
            }
        }
    })
}

/**
 * 上传带进度条，主要是自己实现带进度条的功能RequestBody
 */
fun multipartUploadProgress(
    url: String,
    filePath: String,
    contentType: MediaType? = null,
    params: Map<String, String>? = null,
    block: (UploadState) -> Unit
) {
    var state: UploadState = UploadState.UnStart
    block(state)

    val file = File(filePath)

    val body = MultipartBody.Builder()
        .also {
            params?.forEach { (k, v) ->
                it.addFormDataPart(k, v)
            }
        }.also {
            if (file.exists()) {
                it.addFormDataPart("filename", file.name, file.asProgressRequestBody(contentType, block))
            }
        }.build()

    val request = Request.Builder()
        .url(url)
        .post(body)
//        .addHeader() 可以增加请求头
        .build()

    val client = OkHttpClient.Builder()
        .dispatcher(dispatcher)
        .writeTimeout(30, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.MINUTES)
        .connectTimeout(70, TimeUnit.SECONDS)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            log(e.message)
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                state = UploadState.Complete
                block(state)
            } else {
                log("请求失败")
            }
        }
    })
}


//endregion

//region 通用方法
private fun postCommonRequest(url: String, requestBody: RequestBody, headers: Map<String, String>? = null) {
    val okHttpClient = OkHttpClient.Builder()
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .also { builder: Request.Builder ->
            headers?.forEach { (name, value) ->
                builder.addHeader(name, value)
            }
        }.build()

    okHttpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            log("go failure ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val msg = if (response.isSuccessful) {
//                response.body?.byteStream()?.read()
                simpleDealData(response)
            } else {
                "failure code:${response.code} message:${response.message}"
            }
            log(msg)
        }

    })
}


private fun simpleDealData(response: Response): String = StringBuilder().apply {
    append("\n\t")
    append("header")
    append("\n\t")
    append(response.headers.joinToString("\n\t"))
    append("\n\t")
    append("body")
    append("\n\t")
    append("responseCode: ${response.code}")
    append("\n\t")
    append(
        "content: ${
            (response.body?.string() ?: "").let { s: String ->
                //对获取到的数据 简单做一下格式化
                s.split(",").joinToString("\n\t")
            }
        }"
    )
}.toString()

/**
 * 带进度条上传的功能
 */
private fun File.asProgressRequestBody(contentType: MediaType? = null, block: (UploadState) -> Unit?): RequestBody {
    return object : RequestBody() {
        override fun contentType() = contentType

        override fun contentLength() = length()

        override fun writeTo(sink: BufferedSink) {
            source().use { source ->
                val buffer = Buffer()
                var readCount = 0L
                var progress = 0L
                val progressBlock = UploadState.Progress(contentLength(), progress)
                try {
                    do {
                        if (readCount != 0L) {
                            progress += readCount
                            progressBlock.current = progress
                            sink.write(buffer, readCount)
                            block(progressBlock)
                        }
                        readCount = source.read(buffer, 2048)
                    } while (readCount != -1L)
                } catch (e: Exception) {
//                    e.printStackTrace()
                    block(UploadState.Error(e))
                }
            }
        }
    }
}
//endregion