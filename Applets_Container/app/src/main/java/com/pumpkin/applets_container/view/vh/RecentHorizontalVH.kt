package com.pumpkin.applets_container.view.vh

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhCommonHorizontalBinding
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.AdapterWrapBean

class RecentHorizontalVH(context: Context?, requestManager: RequestManager) :
    CommonHorizontalVH(context, requestManager) {

    override fun customBinding(binding: VhCommonHorizontalBinding) {
//        val container = binding.container
//        val localHeight = 252F.dpToPx
//        container.layoutParams = container.layoutParams.apply {
//            height = localHeight
//        } ?: LinearLayout.LayoutParams(
//            Toolbar.LayoutParams.MATCH_PARENT,
//            localHeight
//        )
    }

    override fun customLayoutManager(context: Context?): RecyclerView.LayoutManager =
        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    override fun bindViewHolder(
        data: List<AdapterWrapBean>?,
        binding: VhCommonHorizontalBinding,
        position: Int
    ) {
        super.bindViewHolder(data, binding, position)
        binding.tvTitle.text = AppUtil.application.getText(R.string.recently_play)
    }

    companion object {
        const val TAG = "RecentHorizontalVH"
        const val TYPE = R.id.vh_recent_horizontal
    }
}