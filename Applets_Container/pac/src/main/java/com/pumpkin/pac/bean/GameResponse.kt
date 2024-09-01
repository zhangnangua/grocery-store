package com.pumpkin.pac.bean

import com.google.gson.JsonObject

class GameResponse (
    var id: String? = null,
    var name: String? = null,
    var link: String? = null,
    var describe: String? = null,
    var icon: String? = null,
    var bigIcon: String? = null,
    var extra: JsonObject? = null,
    var tag: String? = null,
    var orientation: Int? = null,
    var gType: Int? = null
)