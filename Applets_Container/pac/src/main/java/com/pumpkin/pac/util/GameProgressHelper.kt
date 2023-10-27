package com.pumpkin.pac.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameProgressHelper {

    private val progressFlow = MutableStateFlow(0)

    fun getProgressFlow() = progressFlow.asStateFlow()

    suspend fun setProgress(progress: Int) {
        if (progress >= MAX_PROGRESS) {
            progressFinished()
        }
        emitProgress(progress)
    }

    suspend fun progressFinished() {
        emitProgress(MAX_PROGRESS)
    }

    private suspend fun emitProgress(progress: Int) {
        val value = progressFlow.value
        if (progress > value) {
            progressFlow.emit(progress)
        }
    }

    companion object {
        const val MAX_PROGRESS = 100
    }

}