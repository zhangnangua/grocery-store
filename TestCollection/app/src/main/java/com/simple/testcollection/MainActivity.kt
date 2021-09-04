package com.simple.testcollection

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import com.simple.testcollection.databinding.ActivityMainBinding
import com.simple.testcollection.ems.EMSActivity
import kotlinx.coroutines.*
import kotlin.coroutines.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        //exception  test
//        lifecycleScope.launchWhenCreated {
//            testException()
//        }
        val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }
        lifecycleScope.launch(coroutineExceptionHandler) {
            lifecycle.whenCreated {
                testException()
            }
        }
    }

    suspend fun testException() = suspendCoroutine<Unit> {
        it.resumeWithException(IllegalStateException("is exception!"))
    }

    private fun initView() {
        binding.btEms.setOnClickListener {
            startActivity(Intent(this, EMSActivity::class.java))
        }
    }
}