package com.pumpkin.mvvm.widget.exit_dialog

import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.R
import com.pumpkin.mvvm.repo.CollectionKv
import com.pumpkin.ui.util.toShortToast

class DefaultCallback(private val manager: ExitDialogManager, private val callback: ((type: Int) -> Unit)?) {

    fun callback(type: Int) {
        callback?.invoke(type)
        if (type == ExitDialogManager.BT_COLLECTION) {
            if (CollectionKv.alreadySubscribed(manager.id)) {
                CollectionKv.unsubscribe(manager.id)
                manager.unsubscribeUI()
                AppUtil.application.getString(R.string.unsubscribed).toShortToast()
            } else {
                CollectionKv.subscribe(manager.id)
                manager.alreadySubscribedUI()
                AppUtil.application.getString(R.string.already_subscribed).toShortToast()
            }
        }
    }

}