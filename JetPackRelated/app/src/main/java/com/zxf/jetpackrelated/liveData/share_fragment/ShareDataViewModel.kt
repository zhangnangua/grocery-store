package com.zxf.jetpackrelated.liveData.share_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 作者： zxf
 * 描述： 实现fragment数据通信viewModel
 */
class ShareDataViewModel : ViewModel() {

    val progress: MutableLiveData<Int> by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData(0)
    }


    override fun onCleared() {
        super.onCleared()
        // TODO: 2021/9/5
    }
}