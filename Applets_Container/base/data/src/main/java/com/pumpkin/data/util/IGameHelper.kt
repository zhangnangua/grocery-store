package com.pumpkin.data.util

import android.content.Context
import com.pumpkin.data.provider.IGParameter
import com.pumpkin.data.provider.IGame

interface IGameHelper {
    fun openGame(context: Context, game: IGame, gp: IGParameter)

    fun starrRandomNextGame(context: Context)
}