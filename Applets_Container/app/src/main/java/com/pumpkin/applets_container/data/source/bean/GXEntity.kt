package com.pumpkin.applets_container.data.source.bean

import com.pumpkin.mvvm.util.Constant

class GXEntity {
    var data: Data? = null
    var error: List<Any?>? = null

    class Data {
        var games: List<Game?>? = null
        var pagination: Pagination? = null
    }

    class Game {
        var gameId: String? = null
        var gameShortId: String? = null
        var title: String? = null
        var covers: List<Cover?>? = null
        var tags: List<Tag?>? = null
        var shortDescription: String? = null
        var gamePlayStatistics: GamePlayStatistics? = null
        var graphics: List<String?>? = null
    }

    class Tag {
        var tagId: String? = null
        var title: String? = null

    }

    class Cover {
        var coverUrl: String? = null
        var aspectRatio: String? = null
        var type: String? = null
    }

    class GamePlayStatistics {
        var totalPlays: Int = Constant.INVALID_ID
        var totalTime: Long = Constant.INVALID_ID.toLong()
        var uniquePlayers: Int = Constant.INVALID_ID
    }

    class Pagination {
        var currPage: Int = Constant.INVALID_ID
        var numPerPage: Int = Constant.INVALID_ID
        var totalPages: Int = Constant.INVALID_ID
        var totalItems: Int = Constant.INVALID_ID
    }
}



