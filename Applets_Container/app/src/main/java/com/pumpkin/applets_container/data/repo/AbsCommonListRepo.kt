package com.pumpkin.applets_container.data.repo

import com.pumpkin.mvvm.adapter.AdapterWrapBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

abstract class AbsCommonListRepo {
    abstract fun requestFeed(feedFlow: MutableStateFlow<List<AdapterWrapBean>>)
}