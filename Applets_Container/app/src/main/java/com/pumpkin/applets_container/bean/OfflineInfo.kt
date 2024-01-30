package com.pumpkin.applets_container.bean

import com.pumpkin.game.NativeInfo
import com.pumpkin.pac.bean.GameEntity

class OfflineInfo(val gameEntity: GameEntity? = null,
                  val isNative: Boolean = false,
                  val isInternal: Boolean = false,
                  val nativeInfo: NativeInfo? = null)