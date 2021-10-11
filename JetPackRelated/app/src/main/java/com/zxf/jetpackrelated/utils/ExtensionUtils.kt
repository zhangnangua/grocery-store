package com.zxf.jetpackrelated.utils

import kotlin.contracts.contract

/**
 * 作者： zxf
 * 描述： 常用扩展工具
 */

val Float.dpToPx
    get() = dp2px(this)

private fun dp2px(dpValue: Float): Int {
    val scale: Float = AppUtil.application.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun CharSequence?.isNotNullOrEmpty(): Boolean {
    return !isNullOrEmpty()
}
