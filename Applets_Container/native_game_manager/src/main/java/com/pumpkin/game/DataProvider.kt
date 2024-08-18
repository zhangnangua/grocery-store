package com.pumpkin.game

import android.util.ArrayMap

internal object DataProvider {
    private const val pathPrefix = "file:///android_asset/n_icon/"

    private val map = ArrayMap<String, NativeInfo>()

    init {
        map[NativeEntrance.BLOCKS] = blocksInfo()
        map[NativeEntrance.SUPER_MAIN] = superMainInfo()
        map[NativeEntrance.PIXEL_DUNGEON] = pixelDungeonInfo()
    }

    fun obtainInfo(who: String): NativeInfo {
        return map[who] ?: throw IllegalAccessException("not exist $who .")
    }

    fun obtainInfo(): List<NativeInfo> {
        return ArrayList(map.values)
    }

    private fun superMainInfo() =
        NativeInfo(name = "Super Main", icon = "${pathPrefix}super_main.jpeg", who = NativeEntrance.SUPER_MAIN)

    private fun blocksInfo() =
        NativeInfo(name = "Super Blocks", icon = "${pathPrefix}blocks.jpg", who = NativeEntrance.BLOCKS)

    private fun pixelDungeonInfo() =
        NativeInfo(name = "Pixel Dungeon", icon = "${pathPrefix}pixel_dungeon.jpeg", who = NativeEntrance.PIXEL_DUNGEON)


}