package com.zxf.jetpackrelated.liveData.share_fragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zxf.jetpackrelated.databinding.ActivityFragmentContentBinding

/**
 * 作者： zxf
 * 描述： fragment通信 activity
 */
class ShareFragmentActivity : AppCompatActivity() {

    lateinit var binding: ActivityFragmentContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding = ActivityFragmentContentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(binding.flId1.id, SeekBarFragment("One-Fragment")).commit()
        supportFragmentManager.beginTransaction()
            .add(binding.flId2.id, SeekBarFragment("Two-Fragment")).commit()
    }

}