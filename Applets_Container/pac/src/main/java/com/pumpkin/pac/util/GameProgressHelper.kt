package com.pumpkin.pac.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameProgressHelper(private val lifecycle: Lifecycle) {

    private val progressFlow = MutableStateFlow(0)

    fun getProgressFlow() = progressFlow.asStateFlow()

    fun setProgress(progress: Int) {
        if (progress >= MAX_PROGRESS) {
            progressFinished()
        }
        emitProgress(progress)
    }

    fun progressFinished() {
        emitProgress(MAX_PROGRESS)
    }

    private fun emitProgress(progress: Int) {
        val value = progressFlow.value
        if (progress > value) {
            lifecycle.coroutineScope.launch {
                progressFlow.emit(progress)
            }
        }
    }

    companion object {
        const val MAX_PROGRESS = 100
    }

}