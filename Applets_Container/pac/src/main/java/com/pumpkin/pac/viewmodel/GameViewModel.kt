package com.pumpkin.pac.viewmodel

import android.graphics.Bitmap
import android.webkit.WebView
import androidx.lifecycle.ViewModel
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.repo.GameRepo
import com.pumpkin.pac_core.cache2.ResourceInterceptionGlobal
import com.pumpkin.pac_core.webview.PACWebView
import com.pumpkin.pac_core.webview.Webinterface
import com.pumpkin.ui.util.AppUtil
import kotlinx.coroutines.flow.MutableStateFlow

class GameViewModel : ViewModel() {

    private var gameRepo: GameRepo? = null

    internal var interceptionGlobal: ResourceInterceptionGlobal? = null

    /**
     * 进度状态机回调？或者直接给到进度以及webView
     */
    private val stateFlow = MutableStateFlow<GameEntity?>(null)

    fun attach(gameEntity: GameEntity) {
        gameRepo = GameRepo(gameEntity)
        interceptionGlobal = ResourceInterceptionGlobal(AppUtil.application, gameEntity.link)
        interceptionGlobal!!.cacheEnable = true
    }

    fun start(wv: PACWebView) {
        wv.setPageInterface(object : Webinterface.PageInterface {
            override fun onPageFinished(view: WebView?, url: String?) {
                TODO("Not yet implemented")
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                TODO("Not yet implemented")
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                TODO("Not yet implemented")
            }

            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                TODO("Not yet implemented")
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                TODO("Not yet implemented")
            }

        })
    }


    fun check(gameEntity: GameEntity?) {
        if (gameEntity == null) {
            throw IllegalAccessException("game entity is null . ")
        }
    }

    override fun onCleared() {

    }
}