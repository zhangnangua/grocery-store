package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhCarouselItemBinding
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.BaseVHAdapter
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.util.GameHelper

/**
 * 旋转木马VH
 */
class CarouselItemVH(private val context: Context?,private val requestManager: RequestManager) :
    BaseVHAdapter<GameEntity, VhCarouselItemBinding>() {

    override fun createViewHolder(parent: ViewGroup): CommonVH<VhCarouselItemBinding> {
        val binding = VhCarouselItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CommonVH(binding)
    }

    override fun bindViewHolder(data: GameEntity?, binding: VhCarouselItemBinding, position: Int) {
        if (AppUtil.isDebug) {
            Log.d(TAG, "bindViewHolder () ->  $data")
        }
        if (data != null && context != null) {
            requestManager.load(if (TextUtils.isEmpty(data.bigIcon)) data.icon else data.bigIcon)
                .into(binding.carouselImageView)

            binding.root.setOnClickListener {
                GameHelper.openGame(context, data)
            }
        }
    }

    companion object {
        const val TAG = "CarouselVH"
        const val TYPE = R.id.vh_big_card
    }
}
