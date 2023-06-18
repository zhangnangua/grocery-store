package com.pumpkin.applets_container

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.applets_container.databinding.ActivityMainBinding
import com.pumpkin.applets_container.test_multiStateView.TestMultiStateViewActivity
import com.pumpkin.mvvm.util.AppUtil
import com.pumpkin.pac_core.cache2.InterceptorHelper

/**
 * pumpkin
 *
 * 测试
 */
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}