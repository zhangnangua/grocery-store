package com.zxf.utils

import android.util.Log

/**
 * 作者： zxf
 * 描述： log打印封装
 */
object LogUtil {

    //region  日志打印相关
    fun v(log: String) {
        if (isDebug()) {
            Log.v(createTag(), log)
        }
    }

    fun d(log: String) {
        if (isDebug()) {
            Log.d(createTag(), log)
        }
    }

    fun i(log: String) {
        if (isDebug()) {
            Log.i(createTag(), log)
        }
    }

    fun w(log: String) {
        if (isDebug()) {
            Log.w(createTag(), log)
        }
    }

    fun e(log: String) {
        if (isDebug()) {
            Log.e(createTag(), log)
        }
    }


    //endregion
    /**
     * 创建默认tag
     */
    private fun createTag(): String {
        return Exception().stackTrace.let { arrayOfStackTraceElements ->
            val index: Int = 2
            //类名
            val className = arrayOfStackTraceElements[index].className.let {
                val names = it.split(".")
                names[names.size-1]
            }
            //方法名
            val methodName = arrayOfStackTraceElements[index].methodName
            //所在行数
            val lineNumber = arrayOfStackTraceElements[index].lineNumber

            "[$methodName($className:$lineNumber)]"
        }
    }

    /**
     * 是否是debug模式
     */
    private fun isDebug(): Boolean = AppUtil.isDebug

}