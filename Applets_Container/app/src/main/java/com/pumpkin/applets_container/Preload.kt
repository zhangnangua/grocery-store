package com.pumpkin.applets_container

import com.pumpkin.pac.pool.WebViewPool

/**
 * preload related initialization
 */
object Preload {

    fun pacPreload() {
        WebViewPool.preLoad()
    }

}