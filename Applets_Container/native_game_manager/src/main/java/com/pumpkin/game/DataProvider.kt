package com.pumpkin.game

internal object DataProvider {
    private const val pathPrefix = "file:///android_asset/n_icon/"

    fun obtainInfo(who: Int): NativeInfo {
        if (who == NativeEntrance.BLOCKS) {
            return blocksInfo()
        } else if (who == NativeEntrance.SUPER_MAIN) {
            return superMainInfo()
        } else if (who == NativeEntrance.PIXEL_DUNGEON) {
            return pixelDungeonInfo()
        }
        throw IllegalAccessException("not exist $who .")
    }

    private fun superMainInfo() =
        NativeInfo(name = "Super Main", icon = "${pathPrefix}super_main.jpeg", who = NativeEntrance.SUPER_MAIN)

    private fun blocksInfo() =
        NativeInfo(name = "Super Blocks", icon = "${pathPrefix}blocks.jpeg", who = NativeEntrance.BLOCKS)

    private fun pixelDungeonInfo() =
        NativeInfo(name = "Pixel Dungeon", icon = "${pathPrefix}pixel_dungeon.jpeg", who = NativeEntrance.PIXEL_DUNGEON)


}