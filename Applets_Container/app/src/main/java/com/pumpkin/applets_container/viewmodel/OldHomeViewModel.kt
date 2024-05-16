package com.pumpkin.applets_container.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pumpkin.applets_container.data.repo.OldHomeRepo
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import kotlinx.coroutines.flow.Flow

class OldHomeViewModel : ViewModel() {
    private val repo = OldHomeRepo()

    /**
     * 获取数据  分页获取？
     * 1.利用paging返回了冷流
     * 2.cacheIn 是 Flow 的扩展方法，用于将服务器返回的数据在viewModelScope这个作用域内进行缓存，
     * 假如手机横竖屏发生了旋转导致Activity重新创建，Paging 3就可以直接读取缓存中的数据，而不用重新发起网络请求了(使用shareIn转换为了replay = 1的ShareFlow)。
     */
    fun getBigCardPagingData(): Flow<PagingData<AdapterWrapBean>> {
        return repo
            .getBigCardPagingData()
            .cachedIn(viewModelScope)
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }

}