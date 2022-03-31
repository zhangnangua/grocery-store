package com.zxf.jetpackrelated.utils

import android.app.Activity
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator


/**
 * pumpkin
 *
 * 通用工具类
 */

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