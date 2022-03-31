package com.simple.testcollection

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import com.simple.testcollection.databinding.ActivityMainBinding
import com.simple.testcollection.ems.EMSActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        //异常 测试
        exception()
        //HarmonyOs测试
        binding.btHarmonyOS.setOnClickListener {
            harmonyOSTest()
        }
    }

    private fun harmonyOSTest() {
        val isHarmonyOs = try {
            val clz: Class<*> = Class.forName("com.huawei.system.BuildEx")
            val method: java.lang.reflect.Method = clz.getMethod("getOsBrand")
            "harmony" == method.invoke(clz)
        } catch (e: java.lang.Exception) {
            false
        }
        val harmonyOSVersion = try {
            Class.forName("ohos.system.version.SystemVersion").getMethod("getVersion").invoke(null)
        } catch (e: Exception) {
            e.message
        }

        Toast.makeText(
            this,
            isHarmonyOs.toString() + harmonyOSVersion.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun exception() {
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