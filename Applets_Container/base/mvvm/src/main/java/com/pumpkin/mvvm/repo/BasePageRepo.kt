package com.pumpkin.mvvm.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BasePageRepo(private val source: BasePagingSource) {

    fun getPagingData(config: PagingConfig) =
        Pager(config = config, pagingSourceFactory = { source }).flow


    abstract class BasePagingSource : PagingSource<Int, AdapterWrapBean>() {

        override fun getRefreshKey(state: PagingState<Int, AdapterWrapBean>): Int? {
            return null
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AdapterWrapBean> {
            //默认切换到IO
            return withContext(Dispatchers.IO) {
                try {
                    val page = params.key ?: 1
                    val pageSize = params.loadSize

                    val prevKey = if (page > 1) page - 1 else null

                    val data = handlerData(page, pageSize, prevKey)
                    val hasNext = hasNext(data, page, pageSize, prevKey)

                    val nextKey = if (hasNext) page + 1 else null
                    LoadResult.Page(data, prevKey, nextKey)
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }
        }

        abstract suspend fun hasNext(
            data: List<AdapterWrapBean>,
            page: Int,
            pageSize: Int,
            prevKey: Int?
        ): Boolean

        abstract suspend fun handlerData(page: Int, pageSize: Int, prevKey: Int?): List<AdapterWrapBean>

    }
}