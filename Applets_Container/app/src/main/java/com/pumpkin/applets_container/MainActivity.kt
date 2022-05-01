package com.pumpkin.applets_container

import android.content.Intent
import android.os.Bundle
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
            startActivity(Intent(this, TestMultiStateViewActivity::class.java))
        }
    }
}