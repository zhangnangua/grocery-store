package com.zxf.jetpackrelated

import android.content.Intent
import com.zxf.jetpackrelated.databinding.ActivityMainBinding
import com.zxf.jetpackrelated.lifecycle.activityOrfragment.LifecycleActivity

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setEvent()
    }

    private fun setEvent() {
        with(binding) {
            btnGoLifecycleActivity.setOnClickListener {
                startActivity(Intent(this@MainActivity, LifecycleActivity::class.java))
            }
        }
    }
}