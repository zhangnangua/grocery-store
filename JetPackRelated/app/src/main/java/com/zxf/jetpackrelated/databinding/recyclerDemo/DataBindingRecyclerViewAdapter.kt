package com.zxf.jetpackrelated.databinding.recyclerDemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zxf.jetpackrelated.databinding.RecyclerDatabindingItemBinding
import com.zxf.jetpackrelated.databinding.simpleUse.VideoEntity

/**
 * 作者： zxf
 * 描述： 适配器
 */
class DataBindingRecyclerViewAdapter(
    private val datas: List<VideoEntity>
) :
    RecyclerView.Adapter<DataBindingRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerDatabindingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.entityVideo = datas[position]
    }

    override fun getItemCount() = datas.size

    class ViewHolder(val binding: RecyclerDatabindingItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}