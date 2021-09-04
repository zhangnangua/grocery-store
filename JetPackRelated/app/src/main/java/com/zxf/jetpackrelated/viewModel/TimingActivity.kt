package com.zxf.jetpackrelated.viewModel

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.zxf.jetpackrelated.BaseActivity
import com.zxf.jetpackrelated.R
import com.zxf.jetpackrelated.databinding.ActivityCommonBinding

/**
 * 作者： zxf
 * 描述： 简单倒计时demo
 */
class TimingActivity : BaseActivity() {

    lateinit var binding: ActivityCommonBinding

    lateinit var timingViewModel: TimingViewModel

    @SuppressLint("SetTextI18n")
    override fun initView() {
        binding = ActivityCommonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //初始化ViewModel
        timingViewModel = ViewModelProvider(this).get(TimingViewModel::class.java)

        //调用开始计时，并且更新界面
        timingViewModel.startTiming { currentTime ->
            binding.tvContent.text = "currentTime : $currentTime"
        }
    }
}