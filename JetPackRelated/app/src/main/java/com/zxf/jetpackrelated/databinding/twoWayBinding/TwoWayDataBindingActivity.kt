package com.zxf.jetpackrelated.databinding.twoWayBinding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zxf.jetpackrelated.databinding.ActivityDatabindingTwoWayBinding
import com.zxf.jetpackrelated.databinding.recyclerDemo.DataBindingRecyclerViewActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 作者： zxf
 * 描述： 双向绑定
 */
class TwoWayDataBindingActivity : AppCompatActivity() {

    /**
     * binding
     */
    private var _binding: ActivityDatabindingTwoWayBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDatabindingTwoWayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindData()
//        simpleTest()
    }

    private fun bindData() {
        binding.entityObservable = TwoBindingEntityObservable()
        binding.entityObservable2 = TwoBindingEntityObservable2()
        binding.entityObservable3 = TwoBindingEntityObservable3()
    }

    private fun simpleTest() {
        lifecycleScope.launch {
            var counter = 0
            while (true) {
                delay(1000)
                binding.entityObservable?.setDisplayStr((++counter).toString())
                binding.entityObservable2?.displayEntityField?.set(DisplayEntity((++counter).toString()))
                binding.entityObservable3?.displayEntityField?.set((++counter).toString())
            }
        }
    }

    fun goNextTwoBindingActivity(view: View) {
        startActivity(Intent(this, TwoWayDataBindingActivity2::class.java))
    }

    fun goRecyclerDisplay(view: View) {
        startActivity(Intent(this, DataBindingRecyclerViewActivity::class.java))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}