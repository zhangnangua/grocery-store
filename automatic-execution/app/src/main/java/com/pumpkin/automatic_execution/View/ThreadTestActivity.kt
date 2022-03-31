package com.pumpkin.automatic_execution.View

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.automatic_execution.databinding.ActivityThreadTestBinding
import com.pumpkin.automatic_execution.util.toLogI

/**
 * 主线程测试Activity
 */
class ThreadTestActivity : AppCompatActivity() {

    lateinit var binding: ActivityThreadTestBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThreadTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bt1.setOnClickListener {
            Thread {
                binding.tv1.text = "The name of the current thread is ${Thread.currentThread().name}."
            }.apply {
                name = "Myself Thread2"
            }.start()
        }

        //子线程更新text
        Thread {
            binding.tv1.text = "The name of the current thread is ${Thread.currentThread().name}."
        }.apply {
            name = "Myself Thread"
        }.start()
    }

    override fun onResume() {
        super.onResume()
        //打印出当前控件所设置的text
        binding.tv1.text.toString().toLogI()
    }






}