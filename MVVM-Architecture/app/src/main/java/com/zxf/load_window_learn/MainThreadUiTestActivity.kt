package com.zxf.load_window_learn

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zxf.mvvm_architecture.databinding.ActivityMainThreadUiTestBinding
import java.util.concurrent.CountDownLatch

/**
 * 只能主线程更新UI? 测试
 */
class MainThreadUiTestActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainThreadUiTestBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainThreadUiTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        var i = 0
        binding.btTest.setOnClickListener {
            Thread{
                binding.tvTest.text = (++i).toString()
            }.start()
            Log.d("CurrentThread",Thread.currentThread().name)
        }
    }


}