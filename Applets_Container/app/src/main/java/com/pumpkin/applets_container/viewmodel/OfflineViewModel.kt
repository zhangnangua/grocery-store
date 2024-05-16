package com.pumpkin.applets_container.viewmodel

import androidx.lifecycle.ViewModel
import com.pumpkin.applets_container.data.repo.OfflineRepo
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OfflineViewModel : ViewModel() {

    private val repo = OfflineRepo()
    private val feedFlow = MutableStateFlow<List<AdapterWrapBean>>(emptyList())

    fun feed() = feedFlow.asStateFlow()


    fun request() {
        repo.request(feedFlow)
    }

}