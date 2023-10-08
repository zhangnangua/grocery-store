package com.pumpkin.data

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.ArrayMap

/**
 * 作者： pumpkin
 * 描述： appUtil 需要在最外层进行相关初始化操作
 */
object AppUtil {

    private var sProcessName: String? = null

    /**
     * obtain application
     */
    lateinit var application: Application
        private set

    /**
     * 是否是debug模式  在最外层工程中设置
     */
    var isDebug = true
        private set

    /**
     * 在最外层初始化界面的onCreate中设置为false。
     */
    var isKill = true

    /**
     * 额外的参数注入
     */
    val extraParams = ArrayMap<String, Any>()

    fun inject(appApplication: Application, isDebug: Boolean) {
        application = appApplication
        AppUtil.isDebug = isDebug
    }

    fun obtainProcessName(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName()
        }
        return sProcessName ?: try {
            @SuppressLint("PrivateApi") val clz = Class.forName("android.app.ActivityThread")
            @SuppressLint("DiscouragedPrivateApi") val method =
                clz.getDeclaredMethod("currentProcessName")
            val processName = method.invoke(null) as String
            sProcessName = processName
            processName
        } catch (e: Exception) {
            e.printStackTrace()
            if (isDebug) {
                throw e
            } else {
                ""
            }
        }
    }

}