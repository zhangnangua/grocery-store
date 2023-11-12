package com.pumpkin.mvvm.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.RequestManager

private typealias FACTORY = (layoutInflater: LayoutInflater, context: Context, parent: ViewGroup, requestManager: RequestManager) -> BaseVH<Any, ViewBinding>

object TypeHelper {

    private val vhSets = SparseArray<FACTORY?>()

    fun register(type: Int, f: FACTORY) {
        vhSets.append(type, f)
    }

    fun getVH(type: Int, context: Context, parent: ViewGroup, requestManager: RequestManager) =
        vhSets.get(type)?.invoke(LayoutInflater.from(context), context, parent, requestManager)?.apply {
            customBinding(binding, context, requestManager)
        }

}