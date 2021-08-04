package com.simple.dragdeletesample.sample1

import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.simple.dragdeletesample.databinding.ActivitySample1Binding
import com.simple.dragdeletesample.util.dpToPx
import java.util.*

/**
 * 作者： zxf
 */
class Sample1Activity : AppCompatActivity() {
    lateinit var binding: ActivitySample1Binding

    var mAdapter: Sample1Adapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding = ActivitySample1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        mAdapter = Sample1Adapter(this)
        with(binding.rvContent) {
            animation = null
            layoutManager = GridLayoutManager(this@Sample1Activity, 4)
            adapter = mAdapter
        }
        mAdapter?.data = getData()


        //设置recyclerView为最大高度
        binding.rvContent.maxHeight = 500F.dpToPx.toInt()


        binding.tvDelete.setOnDragListener { v, event ->
            println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv = [${v}], event = [${event}]")
            val dragInfo = mAdapter!!.dragInfo
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> { //开始拖动
                    binding.tvDelete.setBackgroundColor(Color.parseColor("#E85653"))
                    mAdapter?.setShowOrHidden(dragInfo.first, false)
                }
                DragEvent.ACTION_DRAG_EXITED -> { //拖动的View从TextView上移除
                    binding.tvDelete.setBackgroundColor(Color.parseColor("#E85653"))
                    mAdapter?.setShowOrHidden(dragInfo.first, false)
                }
                DragEvent.ACTION_DRAG_ENTERED -> { // 拖动的View进入到的TextView上
                    binding.tvDelete.setBackgroundColor(Color.parseColor("#D3504D"))
                    mAdapter?.setShowOrHidden(dragInfo.first, false)
                }
                DragEvent.ACTION_DROP -> { // 在TextView上释放操作
                    binding.tvDelete.setBackgroundColor(Color.parseColor("#E85653"))
                    Toast.makeText(this, "删除咯。。。", Toast.LENGTH_SHORT).show()
                    mAdapter?.data?.let {
                        if (!dragInfo.second) {
                            mAdapter!!.dragInfo = dragInfo.first to true
                            it.removeAt(dragInfo.first)
                            mAdapter?.notifyDataSetChanged()
                        }
                    }
                }
                DragEvent.ACTION_DRAG_ENDED -> {//结束拖动事件
                    binding.tvDelete.setBackgroundColor(Color.parseColor("#E85653"))
                    if (!dragInfo.second) {
                        mAdapter?.setShowOrHidden(dragInfo.first, true)
                    }
                }
            }
            true
        }
    }

    private fun getData(): LinkedList<Pair<Boolean, Int>> {
        return LinkedList<Pair<Boolean, Int>>().also {
            for (i in 0..10) {
                it.add(true to i)
            }
        }
    }
}