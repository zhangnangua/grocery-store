package com.simple.testcollection.ems

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.simple.testcollection.databinding.ActivityEmsBinding


class EMSActivity : AppCompatActivity() {
    lateinit var binding: ActivityEmsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.etInput.doAfterTextChanged {
            binding.tvEms.text = it?.toString() ?: ""
        }
        binding.etEmsNum.doAfterTextChanged {
            val num = try {
                (it?.toString() ?: "5").toInt()
            } catch (e: Exception) {
                5
            }
            binding.tvEms.setEms(num)
        }
    }
}