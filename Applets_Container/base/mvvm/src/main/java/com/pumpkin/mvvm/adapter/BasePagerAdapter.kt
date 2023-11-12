package com.pumpkin.mvvm.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.RequestManager

class BasePagerAdapter(private val requestManager: RequestManager, private val context: Context?) :
    PagingDataAdapter<AdapterWrapBean, BaseVH<Any, ViewBinding>>(COMPARATOR) {

    override fun getItemViewType(position: Int): Int =
        getItem(position)?.type ?: super.getItemViewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<Any, ViewBinding> {
        return TypeHelper.getVH(viewType, parent.context, parent, requestManager)
            ?: throw IllegalStateException("illegal view type .")
    }

    override fun onBindViewHolder(holder: BaseVH<Any, ViewBinding>, position: Int) {
        val wrapBean = try {
            getItem(position)
        } catch (e: Exception) {
            null
        }
        if (wrapBean != null) {
            holder.bindViewHolder(wrapBean.data, holder.binding, position, context, requestManager)
        }
    }

    override fun onViewRecycled(holder: BaseVH<Any, ViewBinding>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    companion object {
        const val TAG = "BaseAdapter"

        private val COMPARATOR = object : DiffUtil.ItemCallback<AdapterWrapBean>() {
            override fun areItemsTheSame(
                oldItem: AdapterWrapBean,
                newItem: AdapterWrapBean
            ): Boolean {
                return false
            }

            override fun areContentsTheSame(
                oldItem: AdapterWrapBean,
                newItem: AdapterWrapBean
            ): Boolean {
                return false
            }
        }
    }
}