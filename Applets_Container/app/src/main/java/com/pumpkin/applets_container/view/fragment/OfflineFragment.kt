package com.pumpkin.applets_container.view.fragment

import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.pumpkin.applets_container.databinding.FragmentOfflineBinding
import com.pumpkin.applets_container.viewmodel.OfflineViewModel
import com.pumpkin.mvvm.adapter.BaseAdapter
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.view.SuperMultiStateBaseFragment
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import com.pumpkin.pac.BuildConfig
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class OfflineFragment : SuperMultiStateBaseFragment() {

    lateinit var binding: FragmentOfflineBinding

    private lateinit var flowAdapter: BaseAdapter

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(this)[OfflineViewModel::class.java]
    }

    override fun initView(view: View) {
        val localContext = context ?: return
        binding = FragmentOfflineBinding.inflate(layoutInflater)
        setPACContentView(binding.root)

        //设置 状态栏 高度
        UIHelper.setStatusHeight(binding.statusBar)

        val spanCount = 2
        val rv = binding.rvContent
        flowAdapter = BaseAdapter(Glide.with(localContext), localContext)
        val layoutManager = GridLayoutManager(localContext, spanCount)
        rv.layoutManager = layoutManager
        rv.adapter = flowAdapter

    }

    override fun loadData() {
        lifecycleScope.launch {
            viewModel.feed().onEach {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "loadData () -> $it")
                }
                flowAdapter.setData(it)
            }.collect()
        }
        viewModel.request()
    }

    companion object {
        const val TAG = "OfflineFragment"
    }
}