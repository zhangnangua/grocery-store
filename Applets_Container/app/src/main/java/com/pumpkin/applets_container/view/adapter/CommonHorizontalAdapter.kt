package com.pumpkin.applets_container.view.adapter

import android.content.Context
import android.util.ArrayMap
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.view.vh.CarouselItemVH
import com.pumpkin.applets_container.view.vh.RecentItemVH
import com.pumpkin.mvvm.adapter.BaseAdapter
import com.pumpkin.mvvm.adapter.IVHAdapter

class CommonHorizontalAdapter(context: Context?, private val requestManager: RequestManager) :
    BaseAdapter(context) {

    override fun provider(context: Context?): ArrayMap<Int, IVHAdapter> {
        return ArrayMap<Int, IVHAdapter>().apply {

            put(CarouselItemVH.TYPE, CarouselItemVH(context, requestManager))

            put(RecentItemVH.TYPE, RecentItemVH(context, requestManager))
        }
    }


}