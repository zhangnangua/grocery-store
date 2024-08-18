package com.pumpkin.applets_container.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pumpkin.applets_container.databinding.FragmentMineBinding
import com.pumpkin.applets_container.viewmodel.MainViewModel
import com.pumpkin.mvvm.adapter.BaseAdapter
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.view.BaseFragment
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MineFragment : BaseFragment() {


    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(this)[MainViewModel::class.java]
    }

    lateinit var binding: FragmentMineBinding

    private lateinit var flowAdapter: BaseAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMineBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val localContext = context ?: return
        //设置 状态栏 高度
        UIHelper.setStatusHeight(binding.statusBar)
        flowAdapter = BaseAdapter(Glide.with(localContext), localContext)
        val layoutManager = object : LinearLayoutManager(localContext) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.rv.layoutManager = layoutManager
        binding.rv.adapter = flowAdapter

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
}