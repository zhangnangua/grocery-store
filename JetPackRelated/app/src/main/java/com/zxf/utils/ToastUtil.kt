package com.zxf.utils

import android.os.Looper
import android.widget.Toast

/**
 * pumpkin
 * 封装，可以在任何线程使用的toast
 */
object ToastUtil {

    /**
     * toastShort
     */
    fun toastShort(str: String) {
        toast(str, Toast.LENGTH_SHORT)
    }

    /**
     * toastLong
     */
    fun toastLong(str: String) {
        toast(str, Toast.LENGTH_LONG)
    }

    /**
     * 可以在任何线程toast
     */
    private fun toast(str: String, duration: Int) {
        var myLooper: Looper? = null
        if (Looper.myLooper() == null) {
            Looper.prepare()
            myLooper = Looper.myLooper()
        }

        Toast.makeText(AppUtil.application, str, duration).show()

        if (myLooper != null) {
            Looper.loop()
            //直接结束掉循环，防止内存泄漏
            myLooper.quit()
        }
    }

}