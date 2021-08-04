package com.simple.dragdeletesample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.simple.dragdeletesample.databinding.ActivityMainBinding
import com.simple.dragdeletesample.sample1.Sample1Activity
import com.simple.dragdeletesample.sample2.Sample2Activity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn1.setOnClickListener(::onClickBtn1)
        binding.btn2.setOnClickListener(::onClickBtn2)
    }

    private fun onClickBtn1(view: View) {
        startActivity(Intent(this, Sample1Activity::class.java))
    }

    private fun onClickBtn2(view: View) {
        startActivity(Intent(this, Sample2Activity::class.java))
    }
}