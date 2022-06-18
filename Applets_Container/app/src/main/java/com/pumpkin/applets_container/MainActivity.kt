package com.pumpkin.applets_container

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.applets_container.test_multiStateView.TestMultiStateViewActivity

/**
 * pumpkin
 *
 * 测试
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO: 2022/5/1 test
        findViewById<Button>(R.id.bt_go_test_multi_state_view).setOnClickListener {
            // TODO: 2022/5/14 ANR 模拟
            Thread.sleep(10000)

            startActivity(Intent(this, TestMultiStateViewActivity::class.java))
        }

//          TODO: 回忆Handler
//        //构建Looper 构建MessageQueue 并将Lopper放入ThreadLocal中
//        Looper.prepare()
//        //从ThreadLocal中获取到Looper，并持有Lopper和对应的MessageQueue
//        val handler = object : Handler() {
//            override fun dispatchMessage(msg: Message) {
//                super.dispatchMessage(msg)
//            }
//        }
//        //Message的target是this，调度到MessageQueue中
//        handler.post { }
//        //开启循环，且调用target的dispatchMessage
//        Looper.loop()
    }
}