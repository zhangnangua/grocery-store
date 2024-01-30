package com.pumpkin.game

internal object DataProvider {
    private const val pathPrefix = "file:///android_asset/n_icon/"

    fun obtainInfo(who: Int): NativeInfo {
        if (who == NativeEntrance.PUZZLE) {
            return puzzleInfo()
        }else if (who == NativeEntrance.SNAKE){
            return snakeInfo()
        }
        throw IllegalAccessException("not exist $who .")
    }

    private fun puzzleInfo() =
        NativeInfo(name = "Puzzle", icon =  "${pathPrefix}puzzle_i.jpeg", who = NativeEntrance.PUZZLE)
    private fun snakeInfo() =
        NativeInfo(name = "Snake", icon =  "", who = NativeEntrance.SNAKE)


}