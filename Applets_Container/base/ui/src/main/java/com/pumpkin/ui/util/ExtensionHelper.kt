package com.pumpkin.ui.util

import android.app.Activity
import android.widget.Toast
import androidx.window.layout.WindowMetricsCalculator

val Float.dpToPx
    get() = dp2px(this)

private fun dp2px(dpValue: Float): Int {
    val scale: Float = AppUtil.application.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun CharSequence?.isNotNullOrEmpty(): Boolean {
    return !isNullOrEmpty()
}

/// region toast打印
fun String?.toShortToast() {
    if (this == null) return

    Toast.makeText(AppUtil.application,this, Toast.LENGTH_SHORT).show()
}

fun String?.toLongToast() {
    if (this == null) return

    Toast.makeText(AppUtil.application,this, Toast.LENGTH_LONG).show()
}

/// region 宽高获取
/**
 * 获取应用当前所占的宽高
 */
fun obtainPhoneCurrentHeight(activity: Activity) = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity).bounds.height()
fun obtainPhoneCurrentWidth(activity: Activity) = WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(activity).bounds.width()

/**
 * 获取物理设备真实测宽高
 */
fun obtainPhoneMaxHeight(activity: Activity) = WindowMetricsCalculator.getOrCreate().computeMaximumWindowMetrics(activity).bounds.height()
fun obtainPhoneMaxWidth(activity: Activity) = WindowMetricsCalculator.getOrCreate().computeMaximumWindowMetrics(activity).bounds.width()

/// endregion width