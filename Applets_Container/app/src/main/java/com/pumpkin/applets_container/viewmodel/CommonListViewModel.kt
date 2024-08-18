package com.pumpkin.applets_container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pumpkin.applets_container.data.repo.AbsCommonListRepo
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CommonListViewModel : ViewModel() {
    private var repo: AbsCommonListRepo? = null
    private val feedFlow = MutableStateFlow<List<AdapterWrapBean>>(emptyList())
    fun attach(repo: AbsCommonListRepo) {
        if (this.repo == null) {
            this.repo = repo
        }
    }

    fun flow() = feedFlow.asStateFlow()

    fun request() {
        repo?.requestFeed(feedFlow)
    }

}