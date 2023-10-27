package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhCommonHorizontalBinding
import com.pumpkin.applets_container.view.adapter.CommonHorizontalAdapter
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.mvvm.adapter.BaseVHAdapter

abstract class CommonHorizontalVH(
    private val context: Context?,
    private val requestManager: RequestManager
) :
    BaseVHAdapter<List<AdapterWrapBean>, VhCommonHorizontalBinding>() {

    override fun createViewHolder(parent: ViewGroup): CommonVH<VhCommonHorizontalBinding> {
        val binding = VhCommonHorizontalBinding.inflate(LayoutInflater.from(context), parent, false)
        val container = binding.container
        container.layoutManager = customLayoutManager(context)
        container.adapter = CommonHorizontalAdapter(context, requestManager)
        customBinding(binding)
        return CommonVH(binding)
    }

    abstract fun customLayoutManager(context: Context?): RecyclerView.LayoutManager?

    override fun bindViewHolder(
        data: List<AdapterWrapBean>?,
        binding: VhCommonHorizontalBinding,
        position: Int
    ) {
        val adapter = binding.container.adapter
        if (adapter is CommonHorizontalAdapter && data != null) {
            adapter.setData(data)
        }
    }

    abstract fun customBinding(binding: VhCommonHorizontalBinding)

    companion object {
        const val TAG = "CommonHorizontalVH"
        const val TYPE = R.id.vh_common_horizontal
    }
}