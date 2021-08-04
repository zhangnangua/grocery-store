package com.simple.dragdeletesample.sample1

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simple.dragdeletesample.databinding.ActicitySample1ItemBinding
import java.util.*

/**
 * 作者： zxf
 */
class Sample1Adapter(private val mContext: Context) :
    RecyclerView.Adapter<Sample1Adapter.Holder>() {

    /**
     * 记录拖动的 item  以及改item是否被移除
     */
    var dragInfo: Pair<Int, Boolean> = -1 to false

    var data: LinkedList<Pair<Boolean, Int>> = LinkedList()
        set(value) {
            field.apply { clear() }.addAll(value)
            notifyDataSetChanged()
        }

    fun setShowOrHidden(position: Int, isShow: Boolean) {
        if (position >= 0) {
            val pair = data[position]
            isShow to pair.second
            data.apply { removeAt(position) }.add(position, isShow to pair.second)
            notifyItemChanged(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return ActicitySample1ItemBinding.inflate(LayoutInflater.from(mContext)).let {
            val holder = Holder(it)
            holder.itemView.setOnLongClickListener { view ->
                //ClipData 剪切板 存放数据  只能在DragEvent.ACTION_DROP的时候拿得到
                val intent = Intent().apply {
                    putExtra("position", holder.adapterPosition)
                }
                val data = ClipData.newIntent("value", intent)

                //设置拖动信息
                dragInfo = holder.adapterPosition to false

                //震动反馈
                view.performHapticFeedback(
                    HapticFeedbackConstants.LONG_PRESS,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(
                        data,
                        MyDragShadowBuilder(view),
                        view,
                        View.DRAG_FLAG_OPAQUE
                    )
                } else {
                    view.startDrag(
                        data,
                        MyDragShadowBuilder(view),
                        view,
                        0
                    )
                }
            }
            holder
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        data[position].let {
            val ivItem = holder.binding.ivItem
            holder.itemView.tag = position
            if (it.first) {
                ivItem.visibility = View.VISIBLE
            } else {
                ivItem.visibility = View.INVISIBLE
            }
        }
    }

    override fun getItemCount() = data.size

    class Holder(val binding: ActicitySample1ItemBinding) : RecyclerView.ViewHolder(binding.root)
}