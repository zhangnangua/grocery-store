package com.pumpkin.data.mmkv

import android.app.Application
import com.getkeepsafe.relinker.ReLinker
import com.tencent.mmkv.MMKV

internal object KvManager {

    private const val ROOT_PATH = "kv_storage"

    @Volatile
    var isInit = false

    private fun init(application: Application) {
        MMKV.initialize(application) { libName -> ReLinker.loadLibrary(application, libName) }

    }


    fun get(
        application: Application,
        name: String,
        mode: Int,
        cryptKey: String? = null
    ): MMKV {
        if (!isInit) {
            synchronized(KvManager::class.java) {
                if (!isInit) {
                    init(application)
                    isInit = true
                }
            }
        }

        return MMKV.mmkvWithID(name, mode, cryptKey, ROOT_PATH)
    }


}