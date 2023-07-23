package com.pumpkin.pac.util

import android.content.Context
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.view.PACActivity

object GameHelper {

    fun openGame(context: Context, gameEntity: GameEntity) {
        PACActivity.go(context, gameEntity)
    }

}