package com.zxf.jetpackrelated.viewModel

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.zxf.jetpackrelated.BaseActivity
import com.zxf.jetpackrelated.R
import com.zxf.jetpackrelated.databinding.ActivityCommonBinding
import kotlinx.coroutines.launch

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

        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            }
        }
    }
}