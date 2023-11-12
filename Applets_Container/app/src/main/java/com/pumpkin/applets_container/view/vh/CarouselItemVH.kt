package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhCarouselItemBinding
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.util.GameHelper

class CarouselItemVH(binding: VhCarouselItemBinding,
                     context: Context?,
                     requestManager: RequestManager)
    : BaseVH<GameEntity, VhCarouselItemBinding>(binding, context, requestManager) {

    override fun bindViewHolder(data: GameEntity?, binding: VhCarouselItemBinding, position: Int, context: Context?, requestManager: RequestManager) {
        if (AppUtil.isDebug) {
            Log.d(TAG, "CarouselItemVH bindViewHolder () ->  $data")
        }
        if (data != null && context != null) {
            requestManager
                .load(if (TextUtils.isEmpty(data.bigIcon)) data.icon else data.bigIcon)
                .into(binding.carouselImageView)

            binding.root.setOnClickListener {
                GameHelper.openGame(context, data)
            }
        }
    }

    override fun customBinding(binding: VhCarouselItemBinding, context: Context?, requestManager: RequestManager) {

    }

    override fun onViewRecycled() {

    }

    companion object {
        const val TAG = "CarouselVH"
        const val TYPE = R.id.vh_carousel
    }
}