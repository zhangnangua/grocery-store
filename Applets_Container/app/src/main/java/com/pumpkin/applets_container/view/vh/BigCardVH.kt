package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhBigCardBinding
import com.pumpkin.applets_container.helper.HomeScrollPlayHelper
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.mvvm.util.EventHelper
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.util.GameHelper
import com.pumpkin.ui.util.dpToPx
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BigCardVH(binding: VhBigCardBinding,
                context: Context?,
                requestManager: RequestManager)
    : BaseVH<GameEntity, VhBigCardBinding>(binding, context, requestManager) {

    private val eventBus = EventHelper.getBus(EventHelper.TYPE_HOME_SCROLL_NOTICE)
    private var job: Job? = null

    override fun customBinding(binding: VhBigCardBinding, context: Context?, requestManager: RequestManager) {

    }

    override fun bindViewHolder(data: GameEntity?, binding: VhBigCardBinding, position: Int, context: Context?, requestManager: RequestManager) {
        if (data != null && context != null) {
            val name = data.name ?: ""
            val describe = data.describe
            with(binding) {
                title.text = name
                secondaryText.text = describe

                val flow = eventBus.getSharedFlow<HomeScrollPlayHelper.ScrollEvent>().onEach {
                    if (AppUtil.isDebug) {
                        Log.d(BigCardVH.TAG, "bindViewHolder () -> position is $position , it.firstNum ${it.firstNum} , it.secondNum ${it.secondNum}")
                    }
                    if (position != it.firstNum && position != it.secondNum) {
                        // no play
                        requestManager
                            .asBitmap()
                            .load(if (TextUtils.isEmpty(data.bigIcon)) data.icon else data.bigIcon)
                            .into(bigIcon)
                        return@onEach
                    }
                    //play
                    requestManager
                        .load(if (TextUtils.isEmpty(data.bigIcon)) data.icon else data.bigIcon)
                        .into(bigIcon)

                }
                cancelJob()
                executeJob(context, flow)

                requestManager
                    .load(data.icon)
                    .transform(RoundedCorners(8F.dpToPx))
                    .into(icon)

                root.setOnClickListener {
                    GameHelper.openGame(context, data)
                }
            }
        }
    }

    private fun executeJob(context: Context?, flow: Flow<HomeScrollPlayHelper.ScrollEvent>) {
        if (context is BaseActivity) {
            job = context.lifecycleScope.launch {
                flow.catch { e ->
                    if (AppUtil.isDebug) {
                        throw java.lang.RuntimeException("big card crash ", e)
                    }
                }.collect()
            }

        }
    }

    override fun onViewRecycled() {
        if (AppUtil.isDebug) {
            Log.d(TAG, "onViewRecycled () -> ")
        }
        cancelJob()
    }

    private fun cancelJob() {
        job?.cancel()
        job = null
    }

    companion object {
        const val TAG = "BigCardVH"
        const val TYPE = R.id.vh_big_card
    }

}