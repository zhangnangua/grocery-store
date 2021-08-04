package com.simple.dragdeletesample.sample2

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.simple.dragdeletesample.databinding.ActivitySample2Binding
import com.simple.dragdeletesample.sample1.Sample1Adapter
import java.util.*

/**
 * 作者： zxf
 * 描述： 描述
 */
class Sample2Activity : AppCompatActivity() {
    lateinit var binding: ActivitySample2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myOnCreate()
    }

    private fun myOnCreate() {
        binding = ActivitySample2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fl2.setRvMaxHeight(500F)
        binding.fl2.setAdapter(Sample1Adapter(this).apply { data = getData() })

        binding.btn2.setOnClickListener(::onBtnClick)
    }

    private fun onBtnClick(v: View) {
        val visibility = binding.fl2.visibility
        if (visibility == View.VISIBLE) {
            binding.fl2.visibility = View.GONE
        } else {
            binding.fl2.visibility = View.VISIBLE
        }

    }


    // TODO: 2021/8/4 TestData
    private fun getData(): LinkedList<Pair<Boolean, Int>> {
        return LinkedList<Pair<Boolean, Int>>().also {
            for (i in 0..10) {
                it.add(true to i)
            }
        }
    }

}