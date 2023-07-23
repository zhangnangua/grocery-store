package com.pumpkin.applets_container.test_multiStateView

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.applets_container.databinding.ActivityTestMultistateViewBinding
import com.pumpkin.ui.widget.MultiStateView

class TestMultiStateViewActivity : AppCompatActivity() {

    lateinit var binding: ActivityTestMultistateViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestMultistateViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.currentState = MultiStateView.ViewState.LOADING
        Thread {
            Thread.sleep(2000)
            Handler(Looper.getMainLooper()).post {
                binding.root.currentState = MultiStateView.ViewState.CONTENT
            }
        }.start()

    }
}