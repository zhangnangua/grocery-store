package com.zxf.jetpackrelated.databinding.twoWayBinding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableInt
import com.zxf.jetpackrelated.databinding.ActivityDatabindingTwoWay2Binding

/**
 * 作者： zxf
 * 描述： 双向绑定2  测试
 */
class TwoWayDataBindingActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityDatabindingTwoWay2Binding

    /**
     * 直接创建可观察 设置初始进度
     */
    val observableProgress = ObservableInt(50)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatabindingTwoWay2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.observableProgress = observableProgress
    }

}