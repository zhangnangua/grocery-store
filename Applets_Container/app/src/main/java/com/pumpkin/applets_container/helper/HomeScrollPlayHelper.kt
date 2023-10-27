package com.pumpkin.applets_container.helper

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pumpkin.applets_container.BuildConfig
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.Event
import com.pumpkin.mvvm.util.EventBus
import com.pumpkin.mvvm.util.EventHelper
import kotlinx.coroutines.launch

class HomeScrollPlayHelper {

    fun startListener(rv: RecyclerView, layoutManager: GridLayoutManager, lifecycleCoroutineScope: LifecycleCoroutineScope) {
        val bus = EventHelper.getBus(EventHelper.TYPE_HOME_SCROLL_NOTICE)
        send(Constant.INVALID_ID, Constant.INVALID_ID, lifecycleCoroutineScope, bus)

        val listener = object : RecyclerView.OnScrollListener() {

            var looperFlag = SCROLL_IDLE

            var lastEvent = ScrollEvent()

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && looperFlag != SCROLL_IDLE) {
                    var first = layoutManager.findFirstCompletelyVisibleItemPosition()
//                    val spanCount = layoutManager.spanCount
                    val spanSize = layoutManager.spanSizeLookup.getSpanSize(first)
                    var second = first + 1
                    if (spanSize != 1) {
                        first = Constant.INVALID_ID
                        second = Constant.INVALID_ID
                    }
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onScrollStateChanged() -> play position:($first,$second)")
                    }
                    if (first == lastEvent.firstNum && second == lastEvent.secondNum) {
                        return
                    }
                    lastEvent = send(first, second, lifecycleCoroutineScope, bus)
                    //注意
                    looperFlag = SCROLL_IDLE
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    //上滑
                    looperFlag = SCROLL_DOWN
                } else if (dy < 0) {
                    //下滑
                    looperFlag = SCROLL_UP
                }
            }
        }

        rv.addOnScrollListener(listener)
    }

    private fun send(first: Int, second: Int, lifecycleCoroutineScope: LifecycleCoroutineScope, bus: EventBus): ScrollEvent {
        val event = ScrollEvent(first, second)
        lifecycleCoroutineScope.launch {
            bus.produceEvent(event)
        }
        return event
    }

    class ScrollEvent(
        val firstNum: Int = Constant.INVALID_ID, val secondNum: Int = Constant.INVALID_ID
    ) : Event

    companion object {
        const val TAG = "HomeScrollPlayHelper"

        const val SCROLL_IDLE = 0
        const val SCROLL_DOWN = 1
        const val SCROLL_UP = 2
    }

}