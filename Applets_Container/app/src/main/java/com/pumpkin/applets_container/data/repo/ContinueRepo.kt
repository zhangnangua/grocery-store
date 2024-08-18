package com.pumpkin.applets_container.data.repo

import com.pumpkin.applets_container.view.vh.CommonListItemVH
import com.pumpkin.data.AppUtil
import com.pumpkin.data.db.DbHelper
import com.pumpkin.data.thread.IoScope
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.pac.bean.tableToEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ContinueRepo : AbsCommonListRepo() {
    private val num = 100
    override fun requestFeed(feedFlow: MutableStateFlow<List<AdapterWrapBean>>) {
        IoScope().launch {
            val tables = DbHelper.providesRecentlyGameDao(AppUtil.application).obtainGame(num)
                ?: return@launch
            val result = mutableListOf<AdapterWrapBean>()
            tables.forEach {
                result.add(AdapterWrapBean(CommonListItemVH.TYPE, it.tableToEntity()))
            }
            feedFlow.emit(result)
        }
    }


}