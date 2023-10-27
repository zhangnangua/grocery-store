package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.bean.TitleEntity
import com.pumpkin.applets_container.databinding.VhTitleBinding
import com.pumpkin.mvvm.adapter.BaseVHAdapter

class TitleVH(private val context: Context?) :
    BaseVHAdapter<TitleEntity, VhTitleBinding>() {
    override fun createViewHolder(parent: ViewGroup): CommonVH<VhTitleBinding> {
        val binding = VhTitleBinding.inflate(LayoutInflater.from(context), parent, false)
        return CommonVH(binding)
    }

    override fun bindViewHolder(data: TitleEntity?, binding: VhTitleBinding, position: Int) {
        if (data != null && context != null) {
            binding.tvTitle.text = data.title
        }
    }

    companion object {
        const val TAG = "TitleVH"
        const val TYPE = R.id.vh_title
    }
}