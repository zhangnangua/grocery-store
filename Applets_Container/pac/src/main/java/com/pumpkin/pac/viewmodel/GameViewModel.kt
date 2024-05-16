package com.pumpkin.pac.viewmodel

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pumpkin.data.AppUtil
import com.pumpkin.pac.bean.GParameter
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.internal.interceptor.InternalGameInterceptor
import com.pumpkin.pac.repo.GameRepo
import com.pumpkin.pac.util.GameProgressHelper
import com.pumpkin.webCache.WVCacheClient
import com.pumpkin.webCache.interceptors.AdvertiseInterceptor
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private var gameRepo: GameRepo? = null

    private val progressHelper = GameProgressHelper()

    private var cacheClient: WVCacheClient? = null


    fun attach(gameEntity: GameEntity, gParameter: GParameter?) {
        gameRepo = GameRepo(gameEntity, gParameter)
        cacheClient = WVCacheClient.Builder(AppUtil.application)
            .originUrl(gameEntity.link)
            .addInterceptor(AdvertiseInterceptor())
            .apply {
                if (gParameter?.isShowLoading == true) {
                    addInterceptor(InternalGameInterceptor())
                }
            }
            .dynamicAbility()
            .build()
    }

    fun getGameEntity() = gameRepo?.gameEntity

    fun getGParameter() = gameRepo?.gParameter

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

    fun resInterceptor(request: WebResourceRequest?): WebResourceResponse? =
        cacheClient?.engine?.interceptRequest(request)

    suspend fun recordToRecently() {
        gameRepo?.recordToRecently()
    }

    override fun onCleared() {

    }
}