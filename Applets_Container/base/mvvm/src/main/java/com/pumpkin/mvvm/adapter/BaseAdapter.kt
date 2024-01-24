package com.pumpkin.mvvm.adapter

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.RequestManager
import com.pumpkin.data.AppUtil

class BaseAdapter(private val requestManager: RequestManager, private val context: Context?) : RecyclerView.Adapter<BaseVH<Any, ViewBinding>>() {

    private val data = mutableListOf<AdapterWrapBean>()

    fun setData(list: List<AdapterWrapBean>) {
        if (AppUtil.isDebug) {
            Log.d(TAG, "setData () -> $list")
        }
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = data[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<Any, ViewBinding> {
        return TypeHelper.getVH(viewType, parent.context, parent, requestManager)
            ?: throw IllegalStateException("illegal view type .")
    }

    override fun onBindViewHolder(holder: BaseVH<Any, ViewBinding>, position: Int) {
        val wrapBean = try {
            data[position]
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

    override fun getItemCount(): Int = data.size

    companion object {
        const val TAG = "BaseAdapter"
    }
}

class AdapterWrapBean(
    val type: Int,
    val data: Any?
) {
    override fun toString(): String {
        return "AdapterWrapBean(type=$type, data=$data)"
    }
}

abstract class BaseVH<in T, B : ViewBinding>(val binding: B,
                                             context: Context?,
                                             requestManager: RequestManager) : RecyclerView.ViewHolder(binding.root) {

    abstract fun customBinding(binding: B, context: Context?, requestManager: RequestManager)

    abstract fun bindViewHolder(data: T?, binding: B, position: Int, context: Context?, requestManager: RequestManager)

    abstract fun onViewRecycled()


}
