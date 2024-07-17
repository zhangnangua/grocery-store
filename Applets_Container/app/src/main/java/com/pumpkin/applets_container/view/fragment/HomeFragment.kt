package com.pumpkin.applets_container.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.pumpkin.applets_container.databinding.FragmentHomeBinding
import com.pumpkin.applets_container.view.activity.SearchActivity
import com.pumpkin.applets_container.view.itemDecoration.GridItemDecoration
import com.pumpkin.applets_container.view.vh.WordCardStyle1VH
import com.pumpkin.applets_container.viewmodel.HomeViewModel
import com.pumpkin.mvvm.adapter.BaseAdapter
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.view.BaseFragment
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import com.pumpkin.ui.util.dpToPx
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment(), View.OnClickListener {

    lateinit var binding: FragmentHomeBinding

    private lateinit var flowAdapter: BaseAdapter

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val localContext = context ?: return

        //设置 状态栏 高度
        UIHelper.setStatusHeight(binding.statusBar)

        val spanCount = 2
        val rv = binding.rvContent
        flowAdapter = BaseAdapter(Glide.with(localContext), localContext)
        val layoutManager = GridLayoutManager(localContext, spanCount).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position > flowAdapter.itemCount - 1) {
                        return spanCount
                    }
                    val type = flowAdapter.getItemViewType(position)
                    return if (type == WordCardStyle1VH.TYPE) {
                        1
                    } else {
                        spanCount
                    }
                }

            }
        }
        val startEndMargin = 16F.dpToPx
        val verticalInterval = 0
        val topMargin = 0
        val itemWidth = 160F.dpToPx
        rv.addItemDecoration(GridItemDecoration(startEndMargin, startEndMargin, itemWidth, topMargin, verticalInterval))
        rv.layoutManager = layoutManager
        rv.adapter = flowAdapter

        binding.etSearch.setOnClickListener(this)
    }

    override fun loadData() {
        lifecycleScope.launch {
            // 每次生命周期处于 STARTED 状态（或更高状态）时，repeatOnLifecycle 在新的协程中启动块，并在它停止时取消它。
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.feed().onEach {
                    flowAdapter.setData(it)
                }.collect()
            }

            //如果这里被执行，则代表生命周期已经走到了onDestroy，因为repeatOnLifecycle是挂起函数，在生命周期为onDestroy的时候进行了恢复。

        }
        viewModel.requestFeed()
    }

    override fun onClick(v: View?) {
        if (v == binding.etSearch) {
            val c = context ?: return
            SearchActivity.go(c)
        }
    }
}




















