package com.pumpkin.applets_container.view.fragment

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.pumpkin.applets_container.databinding.FragmentWorldBinding
import com.pumpkin.applets_container.view.itemDecoration.GridItemDecoration
import com.pumpkin.applets_container.viewmodel.WordViewModel
import com.pumpkin.mvvm.adapter.BaseAdapter
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.view.SuperMultiStateBaseFragment
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import com.pumpkin.ui.util.dpToPx
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WorldFragment : SuperMultiStateBaseFragment() {

    lateinit var binding: FragmentWorldBinding

    private lateinit var flowAdapter: BaseAdapter

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(this)[WordViewModel::class.java]
    }

    override fun initView(view: View) {
        val localContext = context ?: return
        binding = FragmentWorldBinding.inflate(layoutInflater)
        setPACContentView(binding.root)

        //设置 状态栏 高度
        UIHelper.setStatusHeight(binding.statusBar)

        val spanCount = 2
        val rv = binding.rvContent
        flowAdapter = BaseAdapter(Glide.with(localContext), localContext)
        val layoutManager = GridLayoutManager(localContext, spanCount)
        val startEndMargin = 16F.dpToPx
        val verticalInterval = 0
        val topMargin = 10F.dpToPx
        val itemWidth = 160F.dpToPx
        rv.addItemDecoration(GridItemDecoration(startEndMargin, startEndMargin, itemWidth, topMargin, verticalInterval))
        rv.layoutManager = layoutManager
        rv.adapter = flowAdapter

//        lifecycleScope.launch{
//            delay(5000)
//            binding.loading.visibility = View.GONE
//            delay(5000)
//            binding.loading.visibility = View.VISIBLE
//        }
    }

    override fun loadData() {
        lifecycleScope.launch {
            viewModel.feed().onEach {
                flowAdapter.setData(it)
            }.collect()
        }
        viewModel.requestFeed()
    }
}




















