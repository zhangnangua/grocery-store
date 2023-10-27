package com.pumpkin.pac.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pumpkin.data.AppUtil
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.repo.GameRepo
import com.pumpkin.pac.util.GameProgressHelper
import com.pumpkin.pac_core.cache2.ResourceInterceptionGlobal
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private var gameRepo: GameRepo? = null

    private val progressHelper = GameProgressHelper()

    internal var interceptionGlobal: ResourceInterceptionGlobal? = null

    fun attach(gameEntity: GameEntity) {
        gameRepo = GameRepo(gameEntity)
        interceptionGlobal = ResourceInterceptionGlobal(AppUtil.application, gameEntity.link)
        interceptionGlobal!!.cacheEnable = true
    }

    fun getGameEntity() = gameRepo?.gameEntity

    fun getProgressFlow() = progressHelper.getProgressFlow()

    fun setProgress(progress: Int) {
        viewModelScope.launch {
            progressHelper.setProgress(progress)
        }
    }

    fun progressFinished() {
        viewModelScope.launch {
            progressHelper.progressFinished()
        }
    }

    suspend fun recordToRecently() {
        gameRepo?.recordToRecently()
    }

    override fun onCleared() {

    }
}