package com.pumpkin.pac_core.bridge

import android.webkit.JavascriptInterface
import androidx.annotation.Keep

/**
 * JS桥，协议核心交互类
 * @author pumpkin
 */
@Keep
internal object JSBridge {

    /**
     * JS调用JAVA统一入口
     */
    @JavascriptInterface
    fun callPAC(message: String?) {
        // TODO: 2022/4/5 调用处理、鉴权、分发
        // TODO: 2022/4/8 需要处理大数据问题，直接pac_core里面进行处理


    }

}