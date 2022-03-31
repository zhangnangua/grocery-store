package com.pumpkin.automatic_execution.util

import android.widget.Toast

/**
 * 作者： pumpkin
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

/// region log打印扩展
fun String?.toLogV() {
    if (this == null) return

    LogUtil.v(this)
}

fun String?.toLogD() {
    if (this == null) return

    LogUtil.d(this)
}

fun String?.toLogI() {
    if (this == null) return

    LogUtil.i(this)
}

fun String?.toLogW() {
    if (this == null) return

    LogUtil.w(this)
}

fun String?.toLogE() {
    if (this == null) return

    LogUtil.e(this)
}

/// endregion

/// region toast打印
fun String?.toShortToast() {
    if (this == null) return

    Toast.makeText(AppUtil.application,this,Toast.LENGTH_SHORT).show()
}

fun String?.toLongToast() {
    if (this == null) return

    Toast.makeText(AppUtil.application,this,Toast.LENGTH_LONG).show()
}
/// endregion