package com.pumpkin.applets_container.helper

import com.pumpkin.mvvm.util.EventHelper

object RecentlyNoticeHelper {
    val event = EventHelper.getBus(EventHelper.TYPE_RECENTLY)
}