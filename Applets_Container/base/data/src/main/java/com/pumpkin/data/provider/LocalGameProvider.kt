package com.pumpkin.data.provider

object LocalGameProvider {
    val data = arrayListOf<IGame>()

    fun add(c: Collection<IGame>) {
        data.addAll(c)
    }

    fun add(c: IGame) {
        data.add(c)
    }

    fun randomGet(): List<IGame> {
        return ArrayList(data).apply { shuffle() }
    }

    fun randomGetOne(): IGame? {
        val iGames = randomGet()
        return if (iGames.isEmpty()) {
            null
        } else {
            iGames[0]
        }
    }

    fun remove(c: IGame) {
        data.remove(c)
    }

    fun remove(c: Collection<IGame>) {
        data.removeAll(c)
    }

}

interface IGame
interface IGParameter