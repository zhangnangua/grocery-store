package com.zxf.jetpackrelated.databinding.simpleUse

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zxf.jetpackrelated.databinding.ActivityDatabindingBaseBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 作者： zxf
 * 描述： simpleDataBindingActivity
 */
class SimpleDataBindingActivity : AppCompatActivity() {

    private var _binding: ActivityDatabindingBaseBinding? = null
    val binding: ActivityDatabindingBaseBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
    }

    private fun initBinding() {
        _binding = ActivityDatabindingBaseBinding.inflate(layoutInflater)
    }

    private fun initView() {
        setContentView(binding.root)
        //region  使用1
        //布局对实体类进行绑定
//        val videoEntity = VideoEntity()
//        binding.videoEntity = videoEntity
//
//        //简单延时，测试改变
//        lifecycleScope.launch {
//            var i = 0
//            while (i < 5) {
//                delay(2000)
//                i++
//                videoEntity.videoRating = i
//
//                //实体类值改变之后必须重新赋值给binding
//                binding.videoEntity = videoEntity
//            }
//        }
        //endregion


        //region 使用2

        //借助observable实现单向动态绑定
        val videoEntity = VideoEntity()
        //绑定实体类
        binding.videoEntity = videoEntity
        //绑定点击事件
        binding.eventHandler = EventHandleListener(this,videoEntity)

        //简单延时，模拟测试改变
        lifecycleScope.launch {
            var i = 0
            while (i < 5) {
                delay(2000)
                i++
                videoEntity.videoRatingObservable.set(i)
            }
        }
        //endregion

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    /**
     * 事件响应
     */
    inner class EventHandleListener(private val context: Context,private val videoEntity:VideoEntity) {

        fun onButtonClick1(view: View) {
            Toast.makeText(context, "click1", Toast.LENGTH_SHORT).show()
        }

        fun onButtonClick2(view: View) {
            Toast.makeText(context, "click2", Toast.LENGTH_SHORT).show()
        }

        fun onButtonChangePadding(view: View) {
            //设置binding为60
            videoEntity.paddingTestObservable.set(60)
        }
    }
}