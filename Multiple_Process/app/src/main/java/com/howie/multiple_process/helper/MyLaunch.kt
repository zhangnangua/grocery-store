package com.howie.multiple_process.helper

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    // TODO: 异常处理
    throwable.printStackTrace()
}

fun LifecycleOwner.safeLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return lifecycleScope.launch(context + coroutineExceptionHandler, start, block)
}