package com.howie.multiple_process.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.howie.multiple_process.databinding.ActivityMainBinding
import com.howie.multiple_process.view.aidlClient.ClientActivity1
import com.howie.multiple_process.view.aidlClient.ClientActivity2
import com.howie.multiple_process.view.general_lifecycle.GeneralLifecycleActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickEventDeal()
    }


    private fun clickEventDeal() {
        binding.btStartClient1.setOnClickListener {
            startActivity(Intent(this, ClientActivity1::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            })
        }

        binding.btStartClient2.setOnClickListener {
            startActivity(Intent(this, ClientActivity2::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            })
        }

        binding.btStartGeneralLifecycleActivity.setOnClickListener {
            startActivity(Intent(this, GeneralLifecycleActivity::class.java))
        }
    }


}