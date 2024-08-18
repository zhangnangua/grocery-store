package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhCommonListItemBinding
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.util.GameHelper
import com.pumpkin.ui.util.dpToPx

class CommonListItemVH(binding: VhCommonListItemBinding,
                       context: Context?,
                       requestManager: RequestManager)
    : BaseVH<GameEntity, VhCommonListItemBinding>(binding, context, requestManager) {
    override fun customBinding(binding: VhCommonListItemBinding, context: Context?, requestManager: RequestManager) {

    }

    override fun bindViewHolder(data: GameEntity?, binding: VhCommonListItemBinding, position: Int, context: Context?, requestManager: RequestManager) {
        if (data != null && context != null) {
            requestManager
                .load(data.icon)
                .transform(RoundedCorners(8F.dpToPx))
                .into(binding.iv)
            binding.name.text = data.name
            if (TextUtils.isEmpty(data.describe) && TextUtils.isEmpty(data.tag)) {
                binding.subTitle.visibility = View.GONE
            } else {
                binding.subTitle.visibility = View.VISIBLE
                if (TextUtils.isEmpty(data.describe)) {
                    binding.subTitle.text = data.describe
                } else {
                    binding.subTitle.text = data.tag
                }
            }
            binding.root.setOnClickListener {
                GameHelper.openGame(context, data)
            }
        }
    }

    override fun onViewRecycled() {

    }

    companion object {
        const val TAG = "CommonListItemVH"
        const val TYPE = R.id.vh_common_list_item
    }
}