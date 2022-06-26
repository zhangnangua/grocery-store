package com.howie.multiple_process.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.howie.multiple_process.databinding.ActivityClientBinding
import com.howie.multiple_process.writeString
import kotlinx.coroutines.*

class ClientActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityClientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btDescribe.text = this::class.java.simpleName

        doEvent()
    }

    private fun doEvent() {
        //全局异常捕获
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
        binding.btDescribe.setOnClickListener {
            // 测试多进程同一文件写入，client1 写入
            //一般使用lifecycleScope，这里为了测试使用GlobalScope
            GlobalScope.launch(
                context = Dispatchers.Main.immediate + coroutineExceptionHandler,
                start = CoroutineStart.DEFAULT
            ) {
                writeString(content = "i am client2")
            }
        }
    }
}