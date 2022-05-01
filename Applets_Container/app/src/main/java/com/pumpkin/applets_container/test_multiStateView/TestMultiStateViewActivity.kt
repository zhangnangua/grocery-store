package com.pumpkin.applets_container.test_multiStateView

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.applets_container.databinding.ActivityTestMultistateViewBinding

class TestMultiStateViewActivity : AppCompatActivity() {

    lateinit var binding: ActivityTestMultistateViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestMultistateViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}