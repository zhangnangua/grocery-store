package com.zxf.mvvm_architecture

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zxf.load_window_learn.MainThreadUiTestActivity
import com.zxf.mvvm_architecture.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btMainUpdateUiTest.setOnClickListener {
            startActivity(Intent(this, MainThreadUiTestActivity::class.java))
        }

        binding.bt2.setOnClickListener {
            val time = SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(Date()).toString()
            binding.tvDisplay.text = time

            Toast.makeText(this, "AndroidManifest activity label : $title", Toast.LENGTH_LONG).show()
        }
    }


}