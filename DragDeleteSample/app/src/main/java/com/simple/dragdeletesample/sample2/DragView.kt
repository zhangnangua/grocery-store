package com.simple.dragdeletesample.sample2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.simple.dragdeletesample.databinding.ActivitySample1Binding
import com.simple.dragdeletesample.sample1.Sample1Adapter
import com.simple.dragdeletesample.util.dpToPx

/**
 * 作者： dragView
 * 描述： 描述
 */
class DragView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var binding: ActivitySample1Binding

    var mAdapter: Sample1Adapter? = null

    init {
        setBackgroundColor(Color.parseColor("#aa000000"))

        isClickable = true

        binding = ActivitySample1Binding.inflate(LayoutInflater.from(context), this, true)

        //拖拽监听
        binding.tvDelete.setOnDragListener(::dragListener)

        //取消显示
        binding.root.setOnClickListener {
            visibility = GONE
        }
    }

    fun setAdapter(mAdapter: Sample1Adapter) {
        this.mAdapter = mAdapter
        with(binding.rvContent) {
            animation = null
            layoutManager = GridLayoutManager(context, 4)
            adapter = mAdapter
        }
    }


    fun setRvMaxHeight(maxHeight: Float) {
        //设置recyclerView为最大高度
        binding.rvContent.maxHeight = maxHeight.dpToPx.toInt()
    }

    /**
     * drag监听
     */
    private fun dragListener(v: View, event: DragEvent): Boolean {
        val adapter = mAdapter ?: throw IllegalStateException("mAdapter 不允许为null")
        val dragInfo = adapter.dragInfo
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                //开始拖动
                binding.tvDelete.setBackgroundColor(Color.parseColor("#E85653"))
                adapter.setShowOrHidden(dragInfo.first, false)
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                //拖动的View从TextView上移除
                binding.tvDelete.setBackgroundColor(Color.parseColor("#E85653"))
                adapter.setShowOrHidden(dragInfo.first, false)
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                // 拖动的View进入到的TextView上
                binding.tvDelete.setBackgroundColor(Color.parseColor("#D3504D"))
                adapter.setShowOrHidden(dragInfo.first, false)
            }
            DragEvent.ACTION_DROP -> {
                // 在TextView上释放操作
                binding.tvDelete.setBackgroundColor(Color.parseColor("#E85653"))
                Toast.makeText(context, "删除咯。。。", Toast.LENGTH_SHORT).show()
                adapter.data.let {
                    if (!dragInfo.second) {
                        adapter.dragInfo = dragInfo.first to true
                        it.removeAt(dragInfo.first)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                //结束拖动事件
                binding.tvDelete.setBackgroundColor(Color.parseColor("#E85653"))
                if (!dragInfo.second) {
                    adapter.setShowOrHidden(dragInfo.first, true)
                }
            }
        }
        return true
    }
}