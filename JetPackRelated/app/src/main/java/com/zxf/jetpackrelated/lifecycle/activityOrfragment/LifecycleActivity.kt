package com.zxf.jetpackrelated.lifecycle.activityOrfragment

import android.annotation.SuppressLint
import android.content.Intent
import com.zxf.jetpackrelated.BaseActivity
import com.zxf.jetpackrelated.databinding.ActivityCommonBinding
import com.zxf.jetpackrelated.lifecycle.service.MyLifecycleService

/**
 * 作者： zxf
 * 描述： 描述
 */
class LifecycleActivity : BaseActivity() {

    lateinit var binding: ActivityCommonBinding

    //region 不使用lifecycle的方式

//    @SuppressLint("SetTextI18n")
//    override fun initView() {
//        binding = ActivityCommonBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        binding.tvContent.text = "LifecycleActivity"
//        //初始化
//        MockLocationManager.initLocationManager()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        //开始获取地理位置
//        MockLocationManager.startGetLocation()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        //停止获取地理位置
//        MockLocationManager.stopGetLocation()
//    }

    //endregion

    //region 使用lifecycle的方式

//    @SuppressLint("SetTextI18n")
//    override fun initView() {
//        binding = ActivityCommonBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        binding.tvContent.text = "LifecycleActivity"
//
//        //初始化生命周期监听
//        val locationLifecycleListener = LocationLifecycleListener {
//            // TODO: 2021/9/2 接收到位置信息处理
//        }
//        //增加生命周期监听
//        lifecycle.addObserver(locationLifecycleListener)
//    }

    //endregion

    //region 测试fragmentLifecycle

//    override fun initView() {
//        val frameLayout = FrameLayout(this)
//        frameLayout.id = R.id.fragment_id_1
//        setContentView(frameLayout)
//        supportFragmentManager.beginTransaction().add(frameLayout.id, LifecycleFragment()).commit()
//    }

    //endregion


    //region 测试serviceLifecycle

    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding = ActivityCommonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvContent.text = "ServiceActivity"
        //启动服务
        startService(Intent(this, MyLifecycleService::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        //停止服务
        stopService(Intent(this, MyLifecycleService::class.java))
    }

    //endregion
}