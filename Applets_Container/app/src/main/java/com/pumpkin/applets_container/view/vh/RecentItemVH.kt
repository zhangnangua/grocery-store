package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhRecentItemBinding
import com.pumpkin.mvvm.adapter.BaseVHAdapter
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.util.GameHelper
import com.pumpkin.ui.util.dpToPx

class RecentItemVH(private val context: Context?, private val requestManager: RequestManager) :
    BaseVHAdapter<GameEntity, VhRecentItemBinding>() {

    override fun createViewHolder(parent: ViewGroup): CommonVH<VhRecentItemBinding> {
        val binding = VhRecentItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CommonVH(binding)
    }

    override fun bindViewHolder(data: GameEntity?, binding: VhRecentItemBinding, position: Int) {
        if (data != null && context != null) {
            requestManager
                .load(data.icon)
                .transform(RoundedCorners(8F.dpToPx))
                .into(binding.icon)
            binding.text.text = data.name
            binding.root.setOnClickListener {
                GameHelper.openGame(context, data)
            }
        }
    }

    companion object {
        const val TAG = "RecentItemVH"
        const val TYPE = R.id.vh_recent_item
    }
}