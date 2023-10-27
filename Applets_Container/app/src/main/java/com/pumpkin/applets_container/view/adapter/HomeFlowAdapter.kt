package com.pumpkin.applets_container.view.adapter

import android.content.Context
import android.util.ArrayMap
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.view.vh.BigCardVH
import com.pumpkin.applets_container.view.vh.CarouselHorizontalVH
import com.pumpkin.applets_container.view.vh.RecentHorizontalVH
import com.pumpkin.applets_container.view.vh.TitleVH
import com.pumpkin.mvvm.adapter.BasePagerAdapter
import com.pumpkin.mvvm.adapter.IVHAdapter

class HomeFlowAdapter(context: Context?, private val requestManager: RequestManager) :
    BasePagerAdapter(context) {

    override fun provider(context: Context?): ArrayMap<Int, IVHAdapter> {
        return ArrayMap<Int, IVHAdapter>().apply {

            put(TitleVH.TYPE, TitleVH(context))

            put(BigCardVH.TYPE, BigCardVH(context, requestManager))

            put(CarouselHorizontalVH.TYPE, CarouselHorizontalVH(context, requestManager))

            put(RecentHorizontalVH.TYPE, RecentHorizontalVH(context, requestManager))
        }
    }


}