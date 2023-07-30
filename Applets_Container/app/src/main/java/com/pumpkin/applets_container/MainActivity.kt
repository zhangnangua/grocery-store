package com.pumpkin.applets_container

import android.content.Intent
import android.os.Bundle
import com.pumpkin.applets_container.databinding.ActivityMainBinding
import com.pumpkin.applets_container.test_multiStateView.TestMultiStateViewActivity
import com.pumpkin.mvvm.view.SuperBaseActivity
import com.pumpkin.pac.process.connectPool.GameConnectPool
import com.pumpkin.ui.widget.MultiStateView

/**
 * pumpkin
 *
 * 测试
 */
class MainActivity : SuperBaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreateAfter(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setPACContentView(binding.root)
        setRootState(MultiStateView.ViewState.CONTENT)

        binding.btGoTestMultiStateView.setOnClickListener {
            startActivity(Intent(this@MainActivity, TestMultiStateViewActivity::class.java))
        }

        // TODO: 游戏进程预热测试  利用pac进程 爬虫？
        GameConnectPool.connect()

        Thread{
            Thread.sleep(3000)
            GameConnectPool.handle("test")
        }.start()
    }
}