package com.pumpkin.pac.viewmodel

import androidx.lifecycle.ViewModel
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.repo.GameRepo

class GameViewModel : ViewModel() {

    private var gameRepo: GameRepo? = null

    fun attach(gameEntity: GameEntity) {
        gameRepo = GameRepo(gameEntity)
    }



    fun check(gameEntity: GameEntity?) {
        if (gameEntity == null) {
            throw IllegalAccessException("game entity is null . ")
        }
    }

    override fun onCleared() {

    }
}