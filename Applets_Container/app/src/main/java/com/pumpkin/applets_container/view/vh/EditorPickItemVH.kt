package com.pumpkin.applets_container.view.vh

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.bumptech.glide.RequestManager
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.VhEditorPickItemBinding
import com.pumpkin.data.AppUtil
import com.pumpkin.dgx.Launcher
import com.pumpkin.mvvm.adapter.BaseVH
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.pac.bean.GParameter
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.util.GameHelper

class EditorPickItemVH(binding: VhEditorPickItemBinding,
                       context: Context?,
                       requestManager: RequestManager)
    : BaseVH<GameEntity, VhEditorPickItemBinding>(binding, context, requestManager) {

    override fun customBinding(binding: VhEditorPickItemBinding, context: Context?, requestManager: RequestManager) {

    }

    override fun bindViewHolder(data: GameEntity?, binding: VhEditorPickItemBinding, position: Int, context: Context?, requestManager: RequestManager) {
        if (data != null && context != null) {
            requestManager
                .load(if (TextUtils.isEmpty(data.bigIcon)) data.icon else data.bigIcon)
                .into(binding.image)

            binding.name.text = data.name

            binding.root.setOnClickListener {

                context.startActivity(Intent(context, Launcher::class.java))
//                GameHelper.openGame(context, data, GParameter(false, "", data.orientation
//                    ?: Constant.INVALID_ID))
            }

        }
    }

    override fun onViewRecycled() {
        if (AppUtil.isDebug) {
            Log.d(TAG, "onViewRecycled () -> ")
        }

    }

    companion object {
        const val TAG = "EditorPickItemVH"
        const val TYPE = R.id.vh_editor_pick_item
    }

}