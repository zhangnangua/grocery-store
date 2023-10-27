package com.pumpkin.applets_container.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.jvm.internal.Ref.BooleanRef

class MainViewModel : ViewModel() {
    private val drawerFlow = MutableStateFlow(BooleanRef().apply { element = false })

    fun getDrawerFlow() = drawerFlow.asStateFlow()

    @MainThread
    fun openDrawer() {
        internalDrawerControl(BooleanRef().apply { element = true })
    }

    @MainThread
    fun closeDrawer() {
        internalDrawerControl(BooleanRef().apply { element = false })
    }

    private fun internalDrawerControl(isOpen: BooleanRef) {
        drawerFlow.value = isOpen
    }
}