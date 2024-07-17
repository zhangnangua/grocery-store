package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhSearchItemBinding
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.pac.bean.GameEntity

class SearchItemVH(binding: VhSearchItemBinding,
                   context: Context?,
                   requestManager: RequestManager)
    : BaseVH<GameEntity, VhSearchItemBinding>(binding, context, requestManager) {
    override fun customBinding(binding: VhSearchItemBinding, context: Context?, requestManager: RequestManager) {
    }

    override fun bindViewHolder(data: GameEntity?, binding: VhSearchItemBinding, position: Int, context: Context?, requestManager: RequestManager) {
        if (data == null || context == null) {
            return
        }
        requestManager
            .load(data.icon)
            .into(object : DrawableImageViewTarget(binding.ivIcon) {
                var imgWidth: Int = 0
                var imgHeight: Int = 0
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    imgWidth = resource.intrinsicWidth
                    imgHeight = resource.intrinsicHeight
                    super.onResourceReady(resource, transition)
                }

                override fun setResource(resource: Drawable?) {
                    try {
                        val measuredWidth = binding.ivIcon.measuredWidth
                        if (measuredWidth == 0 || imgWidth == 0) {
                            return
                        }
                        val layoutParams = binding.ivIcon.layoutParams
                        val calculateHeight = imgHeight / imgWidth.toFloat() * measuredWidth
                        if (AppUtil.isDebug) {
                            Log.d(TAG, "setResource () -> view width is $measuredWidth imgWidth , $imgWidth , imgHeight , $imgHeight , calculate height $calculateHeight layoutParams $layoutParams")
                        }
                        layoutParams.height = calculateHeight.toInt()
                        layoutParams.width = measuredWidth
                        binding.ivIcon.layoutParams = layoutParams

                        binding.ivIcon.setImageDrawable(resource)
                    } catch (e: Exception) {
                        if (AppUtil.isDebug) {
                            throw e
                        }
                    }
                }
            })

    }

    override fun onViewRecycled() {

    }

    companion object {
        const val TAG = "SearchItemVH"
        const val TYPE = R.id.vh_search_item
    }
}