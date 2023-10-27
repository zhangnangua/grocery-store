package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.widget.LinearLayout
import android.widget.Toolbar.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhCommonHorizontalBinding
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.ui.util.dpToPx
import com.pumpkin.ui.widget.carousel.CarouselLayoutManager

class CarouselHorizontalVH(context: Context?, requestManager: RequestManager) :
    CommonHorizontalVH(context, requestManager) {

    override fun customBinding(binding: VhCommonHorizontalBinding) {
        val container = binding.container
        val localHeight = 200F.dpToPx
        container.layoutParams = container.layoutParams.apply {
            height = localHeight
        } ?: LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            localHeight
        )
    }

    override fun customLayoutManager(context: Context?): RecyclerView.LayoutManager =
        CarouselLayoutManager().apply {

        }

    override fun bindViewHolder(
        data: List<AdapterWrapBean>?,
        binding: VhCommonHorizontalBinding,
        position: Int
    ) {
        super.bindViewHolder(data, binding, position)
        binding.tvTitle.text = AppUtil.application.getText(R.string.top_picks_for_you)
    }

    companion object {
        const val TAG = "CarouselHorizontalVH"
        const val TYPE = R.id.vh_carousel_horizontal
    }
}