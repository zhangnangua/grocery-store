package com.simple.demo.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.simple.demo.databinding.ActivityMainBinding
import com.simple.demo.view.head_image_demo.HeadViewTestDemoActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myOnCreate()
    }

    private fun myOnCreate(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btHeadImage.setOnClickListener {
            startActivity(Intent(this,HeadViewTestDemoActivity::class.java))
        }
    }
}