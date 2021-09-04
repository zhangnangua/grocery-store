package com.zxf.jetpackrelated.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 作者： zxf
 * 描述： simple计时ViewModel
 */
class TimingViewModel : ViewModel() {

    @Volatile
    var currentNum = 0

    /**
     * 回调主界面block
     */
    private lateinit var block: (Int) -> Unit

    /**
     * 倒计时
     * @param block 倒计时回调  调度到主线程执行
     */
    fun startTiming(block: (Int) -> Unit) {
        this.block = block
        //viewModelScope 主从作用域 主线程  在viewModel.onCleared被回调时取消
        viewModelScope.launch(Dispatchers.Default) {
            //防止屏幕旋转多次调用
            if (currentNum <= 0) {

                while (true) {
                    delay(1000)
                    ++currentNum
                    //通知界面刷新
                    withContext(Dispatchers.Main) {
                        this@TimingViewModel.block.invoke(currentNum)
                    }
                }
            }


        }
    }

    override fun onCleared() {
        super.onCleared()
        // TODO: 2021/9/4 可以做资源释放操作
    }
}