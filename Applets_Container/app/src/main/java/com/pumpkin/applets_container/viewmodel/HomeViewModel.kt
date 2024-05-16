package com.pumpkin.applets_container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pumpkin.applets_container.data.repo.HomeRepo
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val repo = HomeRepo()
    private val feedFlow = MutableStateFlow<List<AdapterWrapBean>>(emptyList())

    fun feed() = feedFlow.asStateFlow()

    fun requestFeed() {
        repo.requestFeed(feedFlow, viewModelScope)
    }

}