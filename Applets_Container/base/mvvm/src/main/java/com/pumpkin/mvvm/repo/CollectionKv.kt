package com.pumpkin.mvvm.repo

import com.pumpkin.data.AppUtil
import com.pumpkin.data.mmkv.KV

/**
 * TODO 收藏顺序保证？
 */
object CollectionKv {

    private const val FILE = "pac_collect_data"

    fun alreadySubscribed(id: String): Boolean = getKv().decodeBool(id, false)

    fun subscribe(id: String) {
        getKv().encode(id, true)
    }

    fun unsubscribe(id: String) {
        getKv().remove(id)
    }

    fun allAlreadySubscribed(): List<String> {
        val result = mutableListOf<String>()
        val kv = getKv()
        val allKeys = kv.allKeys() ?: return result
        allKeys.forEach {
            if (kv.decodeBool(it, false)) {
                result.add(it)
            }
        }
        return result
    }

    private fun getKv() = KV.getDefaultModule(AppUtil.application, FILE)
}