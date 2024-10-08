package com.pumpkin.applets_container.view.vh

import android.content.Context
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.bean.OfflineInfo
import com.pumpkin.applets_container.databinding.VhOfflineCardStyle1Binding
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.pac.bean.GParameter
import com.pumpkin.pac.util.GameHelper

class OfflineCardStyle1VH(binding: VhOfflineCardStyle1Binding,
                          context: Context?,
                          requestManager: RequestManager)
    : BaseVH<OfflineInfo, VhOfflineCardStyle1Binding>(binding, context, requestManager) {
    override fun customBinding(binding: VhOfflineCardStyle1Binding, context: Context?, requestManager: RequestManager) {
    }

    override fun bindViewHolder(data: OfflineInfo?, binding: VhOfflineCardStyle1Binding, position: Int, context: Context?, requestManager: RequestManager) {
        if (data == null) {
            return
        }
        if (data.gameEntity != null) {
            val gameEntity = data.gameEntity
            binding.name.text = gameEntity.name
            requestManager.load(gameEntity.icon)
                .into(binding.icon)

            binding.root.setOnClickListener {
                context ?: return@setOnClickListener
                GameHelper.openGame(context, gameEntity, GParameter(notShowLoading = data.isInternal, orientation = gameEntity.orientation
                    ?: Constant.INVALID_ID))
            }
        } else if (data.nativeInfo != null) {
            val nativeInfo = data.nativeInfo
            binding.name.text = nativeInfo.name
            requestManager.load(nativeInfo.icon)
                .into(binding.icon)

            binding.root.setOnClickListener {
                context ?: return@setOnClickListener
                GameHelper.openGame(context, nativeInfo.who)
            }
        }

    }

    override fun onViewRecycled() {
    }

    companion object {
        const val TAG = "WordCardStyle1VH"
        const val TYPE = R.id.vh_offline_card_style_1
    }
}