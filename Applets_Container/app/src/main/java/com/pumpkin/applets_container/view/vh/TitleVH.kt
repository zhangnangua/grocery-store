package com.pumpkin.applets_container.view.vh

import android.content.Context
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.bean.TitleEntity
import com.pumpkin.applets_container.databinding.VhTitleBinding
import com.pumpkin.mvvm.adapter.BaseVH

class TitleVH(binding: VhTitleBinding,
              context: Context?,
              requestManager: RequestManager)
    : BaseVH<TitleEntity, VhTitleBinding>(binding, context, requestManager) {

    override fun bindViewHolder(data: TitleEntity?, binding: VhTitleBinding, position: Int, context: Context?, requestManager: RequestManager) {
        if (data != null && context != null) {
            binding.tvTitle.text = data.title
        }
    }

    override fun customBinding(binding: VhTitleBinding, context: Context?, requestManager: RequestManager) {

    }

    override fun onViewRecycled() {

    }

    companion object {
        const val TAG = "TitleVH"
        const val TYPE = R.id.vh_title
    }
}