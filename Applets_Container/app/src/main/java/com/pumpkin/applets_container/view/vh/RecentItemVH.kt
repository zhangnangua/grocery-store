package com.pumpkin.applets_container.view.vh

import android.content.Context
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhRecentItemBinding
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.util.GameHelper
import com.pumpkin.ui.util.dpToPx

class RecentItemVH(binding: VhRecentItemBinding,
                   context: Context?,
                   requestManager: RequestManager)
    : BaseVH<GameEntity, VhRecentItemBinding>(binding, context, requestManager) {

    override fun bindViewHolder(data: GameEntity?, binding: VhRecentItemBinding, position: Int, context: Context?, requestManager: RequestManager) {
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

    override fun customBinding(binding: VhRecentItemBinding, context: Context?, requestManager: RequestManager) {

    }

    override fun onViewRecycled() {

    }

    companion object {
        const val TAG = "RecentItemVH"
        const val TYPE = R.id.vh_recent_item
    }
}