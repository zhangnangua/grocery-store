package com.pumpkin.automatic_execution

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.automatic_execution.View.PaintActivity
import com.pumpkin.automatic_execution.View.ThreadTestActivity
import com.pumpkin.automatic_execution.databinding.ActivityMainBinding
import com.pumpkin.automatic_execution.util.mockClick
import com.pumpkin.automatic_execution.util.toShortToast
import java.text.SimpleDateFormat
import java.util.*

/**
 * pumpkin
 * 测试自动点击
 */
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        eventRegister()
    }

    private fun eventRegister() {
        binding.btStart.setOnClickListener {
            mockClick(800, 800)
        }
        binding.btAdbClickTest.setOnClickListener {
            "被点击了，当前时间为,${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}".toShortToast()
        }

        //surfaceView 使用测试
        binding.btSurfaceTest.setOnClickListener {
            startActivity(Intent(this, PaintActivity::class.java))
        }

        //线程点击测试
        binding.btThreadClickTest.setOnClickListener {
            startActivity(Intent(this, ThreadTestActivity::class.java))
        }
    }
}