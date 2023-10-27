package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhBigCardBinding
import com.pumpkin.applets_container.helper.HomeScrollPlayHelper
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.BaseVHAdapter
import com.pumpkin.mvvm.util.EventHelper
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.util.GameHelper
import com.pumpkin.ui.util.dpToPx
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BigCardVH(private val context: Context?, private val requestManager: RequestManager) :
    BaseVHAdapter<GameEntity, VhBigCardBinding>() {

    private val eventBus = EventHelper.getBus(EventHelper.TYPE_HOME_SCROLL_NOTICE)

    override fun createViewHolder(parent: ViewGroup): CommonVH<VhBigCardBinding> {
        val binding = VhBigCardBinding.inflate(LayoutInflater.from(context), parent, false)
        return CommonVH(binding)
    }

    override fun bindViewHolder(data: GameEntity?, binding: VhBigCardBinding, position: Int) {
        if (data != null && context != null) {
            val name = data.name ?: ""
            val describe = data.describe
            with(binding) {
                title.text = name
                secondaryText.text = describe

                val flow = eventBus.getSharedFlow<HomeScrollPlayHelper.ScrollEvent>().onEach {
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

                if (context is BaseActivity) {
                    context.lifecycleScope.launch {
                        context.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                            flow.catch { e ->
                                if (AppUtil.isDebug) {
                                    throw java.lang.RuntimeException("big card crash ", e)
                                }
                            }.collect()
                        }
                    }

                }

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

    companion object {
        const val TAG = "BigCardVH"
        const val TYPE = R.id.vh_big_card
    }
}

