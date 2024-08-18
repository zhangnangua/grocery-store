package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhCommonHorizontalBinding
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.pac.bean.tableToEntity
import com.pumpkin.pac.util.RecentlyNoticeHelper
import com.pumpkin.ui.util.dpToPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RecentHorizontalVH(binding: VhCommonHorizontalBinding,
                         context: Context?,
                         requestManager: RequestManager)
    : CommonHorizontalVH(binding, context, requestManager) {

    private var job: Job? = null

    override fun customLayoutManager(context: Context?): RecyclerView.LayoutManager =
        LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

    override fun customCommonBinding(binding: VhCommonHorizontalBinding) {
        binding.ll.apply {
            val dp10 = 10F.dpToPx
            val dp16 = 16F.dpToPx
            setPadding(0, 0, 0, 0)

            (layoutParams as? FrameLayout.LayoutParams)?.apply {
                topMargin = dp10
                marginStart = dp16
                marginEnd = dp16
            }
        }

        binding.tvTitle.textSize = 18F
//        binding.tvTitle.setTextColor(ContextCompat.getColor(AppUtil.application, R.color.colorPrimary))
    }

    override fun bindViewHolder(data: List<AdapterWrapBean>?, binding: VhCommonHorizontalBinding, position: Int, context: Context?, requestManager: RequestManager) {
        val flow = flow().onEach {
            if (AppUtil.isDebug) {
                Log.d(TAG, "$it")
            }
            adapter?.setData(it)
        }
        cancelJob()
        executeJob(context, flow)
        binding.tvTitle.text = AppUtil.application.getText(R.string.recently_play)
    }

    private fun flow() = RecentlyNoticeHelper.recentItemFlow().map {
        ArrayList<AdapterWrapBean>().apply {
            if (AppUtil.isDebug) {
                Log.d(TAG, "flow recent data is $this .")
            }
            it.games.forEach {
                add(AdapterWrapBean(RecentItemVH.TYPE, it.tableToEntity()))
            }
        }
    }.catch { e ->
        if (AppUtil.isDebug) {
            throw RuntimeException("recently e ", e)
        }
    }.flowOn(Dispatchers.IO)

    override fun onViewRecycled() {
//        if (AppUtil.isDebug) {
//            Log.d(BigCardVH.TAG, "onViewRecycled () -> ")
//        }
        cancelJob()
    }

    private fun cancelJob() {
        job?.cancel()
        job = null
    }

    private fun executeJob(context: Context?, flow: Flow<*>) {
        if (context is BaseActivity) {
            job = context.lifecycleScope.launch {
                flow.collect()
            }

        }
    }

    companion object {
        const val TAG = "RecentHorizontalVH"
        const val TYPE = R.id.vh_recent_horizontal
    }
}