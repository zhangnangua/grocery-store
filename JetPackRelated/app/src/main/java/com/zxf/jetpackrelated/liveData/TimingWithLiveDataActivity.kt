package com.zxf.jetpackrelated.liveData

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zxf.jetpackrelated.databinding.ActivityCommonBinding

/**
 * 作者： zxf
 * 描述： 计数器 使用LiveData 通知页面数据刷新
 */
class TimingWithLiveDataActivity : AppCompatActivity() {

    lateinit var binding: ActivityCommonBinding

    lateinit var timingWithLiveDataViewModel: TimingWithLiveDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
        initData()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding = ActivityCommonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btDoAny.visibility = View.VISIBLE
        binding.btDoAny.text = "reset"
    }

    private fun initViewModel() {
        timingWithLiveDataViewModel =
            ViewModelProvider(this)[TimingWithLiveDataViewModel::class.java]
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        //开始倒计时
        timingWithLiveDataViewModel.startTiming()

        timingWithLiveDataViewModel.currentNum.observe(this, { currentNum ->
            //观察到数据变化 更新UI
            binding.tvContent.text = "currentNum:$currentNum"
        })

        //通过LiveData重置数据
        binding.btDoAny.setOnClickListener {
            timingWithLiveDataViewModel.currentNum.value = 0
        }
    }

}