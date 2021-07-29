package com.simple.demo.view.head_image_demo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.simple.demo.databinding.HeadItemBinding
import com.simple.demo.head.loadImage

/**
 * 作者： zxf
 * 描述： item适配器
 */
class ItemAdapter(private val mContext: Context) : RecyclerView.Adapter<ItemAdapter.Holder>() {

    var datas: List<Pair<String, String>>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(HeadItemBinding.inflate(LayoutInflater.from(mContext)))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        datas?.get(position)?.let {
            holder.binding.hivPic.loadImage(it.first, it.second)
            holder.binding.tvPrompt.text = it.second
        }
    }

    override fun getItemCount() = datas?.size ?: 0

    class Holder(val binding: HeadItemBinding) : RecyclerView.ViewHolder(binding.root)
}