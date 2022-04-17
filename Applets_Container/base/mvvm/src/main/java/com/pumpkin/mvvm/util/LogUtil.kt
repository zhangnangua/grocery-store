package com.pumpkin.mvvm.util

import android.util.Log

/**
 * 作者： pumpkin
 * 描述： log打印简单使用
 */

fun String?.toLogV(tag: String = "") {
    if (this == null) return

    v(tag, this)
}

fun String?.toLogD(tag: String = "") {
    if (this == null) return

    d(tag, this)
}

fun String?.toLogI(tag: String = "") {
    if (this == null) return

    i(tag, this)
}

fun String?.toLogW(tag: String = "") {
    if (this == null) return

    w(tag, this)
}

fun String?.toLogE(tag: String = "") {
    if (this == null) return

    e(tag, this)
}

//region  日志打印相关
private fun v(tag: String, log: String) {
    if (isDebug()) {
        Log.v(tag, log)
    }
}

private fun d(tag: String, log: String) {
    if (isDebug()) {
        Log.d(tag, log)
    }
}

private fun i(tag: String, log: String) {
    if (isDebug()) {
        Log.i(tag, log)
    }
}

private fun w(tag: String, log: String) {
    if (isDebug()) {
        Log.w(tag, log)
    }
}

private fun e(tag: String, log: String) {
    if (isDebug()) {
        Log.e(tag, log)
    }
}


//endregion

/**
 * 是否是debug模式
 */
private fun isDebug(): Boolean = AppUtil.isDebug