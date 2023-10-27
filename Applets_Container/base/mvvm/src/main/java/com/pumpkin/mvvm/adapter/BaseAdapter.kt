package com.pumpkin.mvvm.adapter

import android.content.Context
import android.util.ArrayMap
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.pumpkin.data.AppUtil

abstract class BaseAdapter(private val context: Context?) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    IVHProvider {

    private val data = mutableListOf<AdapterWrapBean>()

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

    fun setData(list: List<AdapterWrapBean>) {
        if (AppUtil.isDebug) {
            Log.d(TAG, "setData () -> $list")
        }
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = data[position].type

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
            data[position]
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

    override fun getItemCount(): Int = data.size

    private fun checkInit() {
        if (!init) {
            internalProviderVH(context)
            init = true
        }
    }

    companion object {
        const val TAG = "BaseAdapter"
    }
}

class AdapterWrapBean(
    val type: Int,
    val data: Any?
)

interface IVHProvider {
    fun provider(context: Context?): ArrayMap<Int, IVHAdapter>
}

interface IVHAdapter {

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    fun onBindViewHolder(data: Any?, holder: RecyclerView.ViewHolder, position: Int)

}

abstract class BaseVHAdapter<T : Any, BINDING : ViewBinding> : IVHAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        createViewHolder(parent)

    override fun onBindViewHolder(data: Any?, holder: RecyclerView.ViewHolder, position: Int) {
        bindViewHolder((data as T?), (holder as CommonVH<*>).binding as BINDING, position)
    }

    abstract fun createViewHolder(parent: ViewGroup): CommonVH<BINDING>

    abstract fun bindViewHolder(data: T?, binding: BINDING, position: Int)

    open class CommonVH<BINDING : ViewBinding>(val binding: BINDING) :
        RecyclerView.ViewHolder(binding.root)
}
