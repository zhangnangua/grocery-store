package com.pumpkin.applets_container.data.repo

import com.pumpkin.applets_container.view.vh.CommonListItemVH
import com.pumpkin.data.AppUtil
import com.pumpkin.data.db.DbHelper
import com.pumpkin.data.thread.IoScope
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.mvvm.repo.CollectionKv
import com.pumpkin.pac.bean.tableToEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CollectionRepo : AbsCommonListRepo() {
    override fun requestFeed(feedFlow: MutableStateFlow<List<AdapterWrapBean>>) {
        IoScope().launch {
            val tables = DbHelper.providesGameDao(AppUtil.application).obtainGameByIds(CollectionKv.allAlreadySubscribed())
            val result = mutableListOf<AdapterWrapBean>()
            tables.forEach {
                result.add(AdapterWrapBean(CommonListItemVH.TYPE, it.tableToEntity()))
            }
            feedFlow.emit(result)
        }
    }


}