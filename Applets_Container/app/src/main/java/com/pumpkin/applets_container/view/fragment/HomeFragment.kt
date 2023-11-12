package com.pumpkin.applets_container.view.fragment

import android.util.Log
import android.view.View
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.pumpkin.applets_container.databinding.FragmentHomeBinding
import com.pumpkin.applets_container.helper.HomeScrollPlayHelper
import com.pumpkin.applets_container.view.vh.BigCardVH
import com.pumpkin.applets_container.viewmodel.HomeViewModel
import com.pumpkin.applets_container.viewmodel.MainViewModel
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.BasePagerAdapter
import com.pumpkin.mvvm.adapter.FooterAdapter
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.view.SuperMultiStateBaseFragment
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class HomeFragment : SuperMultiStateBaseFragment() {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(this)[HomeViewModel::class.java]
    }

    private val mainViewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(activity ?: this)[MainViewModel::class.java]
    }

    private lateinit var flowAdapter: BasePagerAdapter

    lateinit var binding: FragmentHomeBinding

    override fun initView(view: View) {
        val localContext = context ?: return
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setPACContentView(binding.root)

        //设置 状态栏 高度
        UIHelper.setStatusHeight(binding.statusBar)

        //drawerLayout设置
        binding.toolBar.setNavigationOnClickListener {
            mainViewModel.openDrawer()
        }

        //rv
        val spanCount = 2
        val rv = binding.rv
        flowAdapter = BasePagerAdapter(Glide.with(localContext), localContext)
        val layoutManager = GridLayoutManager(localContext, spanCount).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position > flowAdapter.itemCount - 1) {
                        return spanCount
                    }
                    val type = flowAdapter.getItemViewType(position)
                    return if (type == BigCardVH.TYPE) {
                        1
                    } else {
                        spanCount
                    }
                }

            }
        }
        rv.layoutManager = layoutManager
        binding.rv.adapter = flowAdapter.withLoadStateFooter(FooterAdapter())
        //scroll register
        HomeScrollPlayHelper().startListener(rv, layoutManager, lifecycleScope)

        //监听加载状态
        flowAdapter.addLoadStateListener {
            //比如处理下拉刷新逻辑
            when (it.refresh) {
                is LoadState.NotLoading -> {

                }
                is LoadState.Loading -> {

                }
                is LoadState.Error -> {
                    if (AppUtil.isDebug) {
                        val state = it.refresh as LoadState.Error
                        Log.d(TAG, "loadData () -> e is ${state.error.message}")
                    }
                }
            }
        }
    }

    override fun loadData() {
        context ?: return
        lifecycle.coroutineScope.launch {
            viewModel.getBigCardPagingData()
                .catch { e ->
                    if (AppUtil.isDebug) {
                        Log.d(TAG, "loadData () -> e is ${e.message}")
                    }
                }
                .onEach {
                    flowAdapter.submitData(it)
                }.collect()
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}