package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhCommonHorizontalBinding
import com.pumpkin.applets_container.view.itemDecoration.HorizontalInternalItemDecoration
import com.pumpkin.ui.util.dpToPx

class EditorPickHorizontalVH(binding: VhCommonHorizontalBinding,
                             context: Context?,
                             requestManager: RequestManager)
    : CommonHorizontalVH(binding, context, requestManager) {
    override fun customLayoutManager(context: Context?): RecyclerView.LayoutManager =
        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)


    override fun customCommonBinding(binding: VhCommonHorizontalBinding) {
        binding.tvTitle.visibility = View.GONE
        val dp16 = 11F.dpToPx
        binding.container.addItemDecoration(HorizontalInternalItemDecoration(dp16, dp16, 10F.dpToPx))
    }

    companion object {
        const val TAG = "NoTitleHorizontalVH"
        const val TYPE = R.id.vh_no_title_horizontal
    }
}