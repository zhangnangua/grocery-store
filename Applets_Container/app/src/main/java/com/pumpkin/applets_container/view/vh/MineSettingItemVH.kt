package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar.LayoutParams
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.pumpkin.applets_container.BuildConfig
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.bean.SettingEntity
import com.pumpkin.applets_container.databinding.LayoutSettingItemBinding
import com.pumpkin.applets_container.helper.GDPRHelper
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.mvvm.util.WidgetUtil
import com.pumpkin.pac.view.BrowserActivity
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
                GDPRHelper.goPrivacyPolicy(context)
            } else if (data.title == R.string.terms_of_service) {
                GDPRHelper.goUserAgreement(context)
            } else if (data.title == R.string.version_info) {
                val content = AppUtil.application.getString(R.string.game_box_version, BuildConfig.VERSION_NAME)
                Snackbar.make(binding.root, content, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.copy) {
                        WidgetUtil.copyTextToClipboard(text = content)
                    }
                    .show()
            } else if (data.title == R.string.feed_back) {
                BrowserActivity.go(context, "file:///android_asset/feedback/feedback.html")
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