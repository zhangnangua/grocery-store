package com.pumpkin.applets_container.view.vh

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhCommonHorizontalBinding
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.mvvm.adapter.BaseAdapter
import com.pumpkin.mvvm.adapter.BaseVH

abstract class CommonHorizontalVH(binding: VhCommonHorizontalBinding,
                                  context: Context?,
                                  requestManager: RequestManager)
    : BaseVH<List<AdapterWrapBean>, VhCommonHorizontalBinding>(binding, context, requestManager) {

    protected var adapter: BaseAdapter? = null

    override fun customBinding(binding: VhCommonHorizontalBinding, context: Context?, requestManager: RequestManager) {
        val container = binding.container
        container.layoutManager = customLayoutManager(context)
        container.adapter = BaseAdapter(requestManager, context).apply {
            adapter = this
        }
        customCommonBinding(binding)
    }

    override fun bindViewHolder(data: List<AdapterWrapBean>?, binding: VhCommonHorizontalBinding, position: Int, context: Context?, requestManager: RequestManager) {

        val adapter = binding.container.adapter
        if (adapter is BaseAdapter && data != null) {
            adapter.setData(data)
        }

    }

    override fun onViewRecycled() {

    }

    abstract fun customLayoutManager(context: Context?): RecyclerView.LayoutManager?

    abstract fun customCommonBinding(binding: VhCommonHorizontalBinding)


    companion object {
        const val TAG = "CommonHorizontalVH"
        const val TYPE = R.id.vh_common_horizontal
    }
}