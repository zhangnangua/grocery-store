package com.pumpkin.mvvm.adapter

import android.content.Context
import android.util.ArrayMap
import android.util.Log
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pumpkin.data.AppUtil

abstract class BasePagerAdapter(private val context: Context?) :
    PagingDataAdapter<AdapterWrapBean, RecyclerView.ViewHolder>(COMPARATOR),
    IVHProvider {

    private val vhSets = ArrayMap<Int, IVHAdapter>()

    private var init = false

    private fun internalProviderVH(context: Context?) {
        vhSets.clear()
        val providers = provider(context)
        providers.forEach {
            vhSets[it.key] = it.value
            if (AppUtil.isDebug) {
                Log.d(TAG, "internalProviderVH () -> ${it.key}  &&  ${it.value}")
            }
        }

    }

    override fun getItemViewType(position: Int): Int =
        getItem(position)?.type ?: super.getItemViewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (AppUtil.isDebug) {
            Log.d(TAG, "internalProviderVH () -> $vhSets")
        }
        checkInit()
        val vh = vhSets[viewType] ?: throw IllegalStateException("illegal view type .")
        return vh.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val wrapBean = try {
            getItem(position)
        } catch (e: Exception) {
            null
        }
        checkInit()
        if (wrapBean != null) {
            val ivhAdapter =
                vhSets[wrapBean.type] ?: throw IllegalStateException("illegal view type .")
            ivhAdapter.onBindViewHolder(wrapBean.data, holder, position)
        }
    }

    private fun checkInit() {
        if (!init) {
            internalProviderVH(context)
            init = true
        }
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