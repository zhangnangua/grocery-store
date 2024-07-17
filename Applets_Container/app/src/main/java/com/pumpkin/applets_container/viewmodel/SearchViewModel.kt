package com.pumpkin.applets_container.viewmodel

import androidx.lifecycle.ViewModel
import com.pumpkin.applets_container.data.repo.SearchRepo
import com.pumpkin.data.thread.IoScope
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel : ViewModel() {
    private val repo = SearchRepo()
    private val flow = MutableStateFlow<List<AdapterWrapBean>>(emptyList())

    fun flow() = flow.asStateFlow()

    fun requestFeed(search: String?) {
        repo.search(flow, IoScope(), search)
    }

}