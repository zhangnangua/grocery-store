package com.pumpkin.applets_container.view.vh

import android.content.Context
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.data.repo.CollectionRepo
import com.pumpkin.applets_container.data.repo.ContinueRepo
import com.pumpkin.applets_container.databinding.VhMineNavigationLayoutBinding
import com.pumpkin.applets_container.view.activity.CommonListActivity
import com.pumpkin.applets_container.view.activity.CommonListParameterBuilder
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.BaseVH

class MineNavigationVH(binding: VhMineNavigationLayoutBinding,
                       context: Context?,
                       requestManager: RequestManager)
    : BaseVH<Any, VhMineNavigationLayoutBinding>(binding, context, requestManager) {

    override fun bindViewHolder(data: Any?, binding: VhMineNavigationLayoutBinding, position: Int, context: Context?, requestManager: RequestManager) {
        if (data == null || context == null) {
            return
        }

        binding.playRecord.setOnClickListener {
            CommonListActivity.go(context, CommonListParameterBuilder()
                .setRepo(ContinueRepo::class.java)
                .setTitle(AppUtil.application.getString(R.string.play_record)))
        }

        binding.myCollect.setOnClickListener {
            CommonListActivity.go(context, CommonListParameterBuilder()
                .setRepo(CollectionRepo::class.java)
                .setTitle(AppUtil.application.getString(R.string.my_collection)))
        }

    }

    override fun customBinding(binding: VhMineNavigationLayoutBinding, context: Context?, requestManager: RequestManager) {

    }

    override fun onViewRecycled() {

    }

    companion object {
        const val TAG = "MineNavigationVH"
        const val TYPE = R.id.vh_mine_navigation
    }
}