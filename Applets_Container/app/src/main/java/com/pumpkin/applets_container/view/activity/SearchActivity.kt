package com.pumpkin.applets_container.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.pumpkin.applets_container.databinding.ActivitySearchBinding
import com.pumpkin.applets_container.view.itemDecoration.GridItemDecoration
import com.pumpkin.applets_container.viewmodel.SearchViewModel
import com.pumpkin.data.AppUtil
import com.pumpkin.data.thread.ThreadHelper
import com.pumpkin.mvvm.R
import com.pumpkin.mvvm.adapter.BaseAdapter
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import com.pumpkin.ui.util.dpToPx
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


/**
 * search activity
 */
class SearchActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivitySearchBinding
    private lateinit var flowAdapter: BaseAdapter

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(this)[SearchViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            // 每次生命周期处于 STARTED 状态（或更高状态）时，repeatOnLifecycle 在新的协程中启动块，并在它停止时取消它。
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flow().onEach {
                    flowAdapter.setData(it)
                }.collect()
            }
            //如果这里被执行，则代表生命周期已经走到了onDestroy，因为repeatOnLifecycle是挂起函数，在生命周期为onDestroy的时候进行了恢复。

        }
    }

    private fun initView() {
        //设置 状态栏 高度
        UIHelper.setStatusHeight(binding.statusBar)
        ThreadHelper.runOnUiThreadDelay({
            binding.etSearch.showKeyboard()
        }, 500)
        binding.ivBack.setOnClickListener(this)
        binding.searchBtn.setOnClickListener(this)

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE)
//        val startEndMargin = 16F.dpToPx
//        val verticalInterval = 0
//        val topMargin = 0
//        val itemWidth = 160F.dpToPx
//        rv.addItemDecoration(GridItemDecoration(startEndMargin, startEndMargin, itemWidth, topMargin, verticalInterval))
        binding.rv.layoutManager = staggeredGridLayoutManager
        flowAdapter = BaseAdapter(Glide.with(this), this)
        binding.rv.adapter = flowAdapter
    }

    override fun onClick(v: View?) {
        if (v == binding.ivBack) {
            binding.etSearch.hideKeyboard()
            finishPage()
        } else if (v == binding.searchBtn) {
            search(binding.etSearch.getSearchText())
        }
    }

    private fun search(search: String) {
        if (AppUtil.isDebug) {
            Log.d(TAG, "search () -> $search")
        }
        // TODO: 防抖处理
        viewModel.requestFeed(search)
    }

    private fun finishPage() {
        binding.etSearch.hideKeyboard()
        finish()
    }

    companion object {
        private const val TAG = "SearchActivity"
        fun go(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java).apply {
                putExtra(Constant.PAGE_PARAMETER, ActivitySettingBean().apply {
                    enterAnim = R.anim.slide_no_process
                    exitAnim = R.anim.slide_no_process
                })
            })
        }

    }
}