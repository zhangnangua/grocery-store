package com.pumpkin.applets_container.view.vh

import android.content.Context
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhWordCardStyle1Binding
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.pac.bean.WordCardStyle
import com.pumpkin.pac.view.BrowserActivity
import com.pumpkin.ui.util.dpToPx

class WordCardStyle1VH(binding: VhWordCardStyle1Binding,
                       context: Context?,
                       requestManager: RequestManager)
    : BaseVH<WordCardStyle, VhWordCardStyle1Binding>(binding, context, requestManager) {
    override fun customBinding(binding: VhWordCardStyle1Binding, context: Context?, requestManager: RequestManager) {
    }

    override fun bindViewHolder(data: WordCardStyle?, binding: VhWordCardStyle1Binding, position: Int, context: Context?, requestManager: RequestManager) {
        if (data == null) {
            return
        }

        binding.tvName.text = data.name
        requestManager.load(data.icon)
            .transform(RoundedCorners(16F.dpToPx))
            .into(binding.ivImage)

        binding.root.setOnClickListener {
            context ?: return@setOnClickListener
            BrowserActivity.go(context, data)
        }
    }

    override fun onViewRecycled() {

    }

    companion object {
        const val TAG = "WordCardStyle1VH"
        const val TYPE = R.id.vh_word_card_style_1
    }
}