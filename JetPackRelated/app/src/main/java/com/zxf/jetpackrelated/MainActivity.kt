package com.zxf.jetpackrelated

import android.content.Intent
import com.zxf.jetpackrelated.databinding.ActivityMainBinding
import com.zxf.jetpackrelated.lifecycle.activityOrfragment.LifecycleActivity
import com.zxf.jetpackrelated.liveData.TimingWithLiveDataActivity
import com.zxf.jetpackrelated.liveData.share_fragment.ShareFragmentActivity
import com.zxf.jetpackrelated.room.baseUse.SimpleRoomDemoActivity
import com.zxf.jetpackrelated.viewModel.TimingActivity

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setEvent()
    }

    private fun setEvent() {
        with(binding) {
            //lifecycle
            btnGoLifecycleActivity.setOnClickListener {
                startActivity(LifecycleActivity::class.java)
            }
            //viewModel 简单倒计时 demo
            btnGoViewmodelTimingActivity.setOnClickListener {
                startActivity(TimingActivity::class.java)
            }
            //liveData  计数器demo 改进
            btnGoLivedataTimingActivity.setOnClickListener {
                startActivity(TimingWithLiveDataActivity::class.java)
            }
            //liveData fragment 共享数据
            btnGoLivedataShareFragment.setOnClickListener {
                startActivity(ShareFragmentActivity::class.java)
            }
            //room 简单使用demo
            btnGoRoomSimpleActivity.setOnClickListener {
                startActivity(SimpleRoomDemoActivity::class.java)
            }
        }
    }

    /**
     * startActivity 简单封装
     */
    private fun <T> startActivity(cls: Class<T>) {
        startActivity(Intent(this@MainActivity, cls))
    }
}