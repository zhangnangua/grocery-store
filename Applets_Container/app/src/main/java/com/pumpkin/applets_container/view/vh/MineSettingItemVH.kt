package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar.LayoutParams
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.bean.SettingEntity
import com.pumpkin.applets_container.databinding.LayoutSettingItemBinding
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.ui.util.dpToPx

class MineSettingItemVH(binding: LayoutSettingItemBinding,
                        context: Context?,
                        requestManager: RequestManager)
    : BaseVH<SettingEntity, LayoutSettingItemBinding>(binding, context, requestManager) {

    override fun bindViewHolder(data: SettingEntity?, binding: LayoutSettingItemBinding, position: Int, context: Context?, requestManager: RequestManager) {
        if (data == null || context == null) {
            return
        }

        binding.text.text = AppUtil.application.getText(data.title)
        binding.icon.setBackgroundResource(data.icon)
        if (data.marginTop > 0) {
            val layoutParams = (binding.rl.layoutParams as? FrameLayout.LayoutParams)
                ?: FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 60F.dpToPx)
            layoutParams.topMargin = data.marginTop
            binding.rl.layoutParams = layoutParams
        }

        binding.root.setOnClickListener { view ->
            if (data.title == R.string.privacy_policy) {

            } else if (data.title == R.string.terms_of_service) {

            } else if (data.title == R.string.version_info) {

            }
        }

    }

    override fun customBinding(binding: LayoutSettingItemBinding, context: Context?, requestManager: RequestManager) {

    }

    override fun onViewRecycled() {

    }

    companion object {
        const val TAG = "MineSettingVH"
        const val TYPE = R.id.vh_mine_setting_item
    }
}