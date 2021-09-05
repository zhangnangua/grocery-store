package com.zxf.jetpackrelated.liveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 作者： zxf
 * 描述： 计数器 ViewModel  使用 LiveData 通知页面
 */
class TimingWithLiveDataViewModel : ViewModel() {

    /**
     * 当前的计数 初始值为0
     */
    val currentNum: MutableLiveData<Int> = MutableLiveData(0)

    /**
     * job
     */
    var job: Job? = null

    /**
     * 开始计数
     */
    fun startTiming() {
        if (job == null) {
            job = viewModelScope.launch(Dispatchers.Default) {
                while (true) {
                    delay(1000)
                    var value = currentNum.value ?: 0
                    currentNum.postValue(++value)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        job = null
    }

}