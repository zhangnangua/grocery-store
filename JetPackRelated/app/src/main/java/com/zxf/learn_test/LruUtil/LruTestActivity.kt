package com.zxf.learn_test.LruUtil

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zxf.jetpackrelated.databinding.ActivityLruTestBinding
import com.zxf.jetpackrelated.utils.toShortToast
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * LruCache 使用测试界面
 */
class LruTestActivity : AppCompatActivity() {

    /**
     * 简单使用一下SharedFlow
     */
    private val sharedFlow = MutableSharedFlow<LruCache<String, String>>(
        replay = 0,
        extraBufferCapacity = 0,
        BufferOverflow.SUSPEND
    )

    val lruCache = LruCache<String, String>(15)

    lateinit var binding: ActivityLruTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLruTestBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lruCache.customSizeOf = { _, value ->
            value.length
        }

        //绑定点击事件
        event()

        sharedFlow.onEach {
            //展示
            binding.tvDisplay.text = it.toString()
        }.launchIn(lifecycleScope)
    }

    fun event() {
        binding.bt1.setOnClickListener {
            val text = binding.etInput1.text.toString()
            lruCache[text] = text

            send()
        }
        binding.bt1Get.setOnClickListener {
            val text = binding.etInput1.text.toString()
            //获取缓存的值
            val s = lruCache[text]
            "获取到当前的值为$s".toShortToast()

            send()
        }

        binding.bt1ClearAll.setOnClickListener {
            //清除所有缓存
            lruCache.clearAll()

            send()
        }
        binding.bt2.setOnClickListener {
            //改变lru 的最大size
            lruCache.maxSize = binding.etInput2.text.toString().toInt()

            send()
        }
        binding.bt3.setOnClickListener {
            // TODO: 2022/4/11 hashvalue 测试
            System.identityHashCode("测试我的hash值").toString().toShortToast()
        }
        binding.bt4.setOnClickListener {
            // TODO: 2022/4/11 子线程异常测试
            Thread{
                val dividend = 0
                val divisor = 1
                val result = divisor/dividend
            }.start()
        }
    }

    private fun send() {
        lifecycleScope.launch {
            sharedFlow.emit(lruCache)
        }
    }


}