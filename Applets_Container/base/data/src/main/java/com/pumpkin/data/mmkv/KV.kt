package com.pumpkin.data.mmkv

import android.app.Application
import com.tencent.mmkv.MMKV

object KV {

    private const val DEFAULT_FILE = "pac_common"

    fun getDefaultModule(
        application: Application,
        fileName: String = DEFAULT_FILE,
        mode: Int = MMKV.MULTI_PROCESS_MODE
    ): MMKV {
        return KvManager.get(application, fileName, mode)
    }

}