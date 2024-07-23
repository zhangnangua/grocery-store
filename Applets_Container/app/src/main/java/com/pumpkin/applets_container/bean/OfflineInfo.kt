package com.pumpkin.applets_container.bean

import com.pumpkin.game.NativeInfo
import com.pumpkin.pac.bean.GameEntity

class OfflineInfo(val isNative: Boolean = false,
                  val nativeInfo: NativeInfo? = null,
                  val gameEntity: GameEntity? = null,
                  val isInternal: Boolean = false)