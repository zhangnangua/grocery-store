package com.pumpkin.applets_container.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.navigation.NavigationBarView
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.ActivityMain1Binding
import com.pumpkin.applets_container.view.fragment.HomeFragment
import com.pumpkin.applets_container.view.fragment.OfflineFragment
import com.pumpkin.applets_container.view.fragment.OldHomeFragment
import com.pumpkin.applets_container.viewmodel.MainViewModel
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.mvvm.view.BaseFragment
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import com.pumpkin.pac.process.connectPool.PACProcessClient


/**
 * pumpkin
 *
 * 测试
 */
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMain1Binding
    var currentId = R.id.home

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 启动主题
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val bundle = savedInstanceState ?: intent?.extras
        val firstParameter = bundle?.getInt(Constant.FIRST_PARAMETER, Constant.INVALID_ID)
        currentId = if ((firstParameter == null || firstParameter == Constant.INVALID_ID)) {
            R.id.home
        } else {
            firstParameter
        }

        binding = ActivityMain1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        loadData()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        val newCurrentId = intent?.extras?.getInt(Constant.FIRST_PARAMETER, Constant.INVALID_ID)
        if (newCurrentId != null && newCurrentId != Constant.INVALID_ID) {
            currentId = newCurrentId
        }
    }

    private fun initView() {
        //底部导航栏
        val onItemSelectedListener = navigationFragment()
        binding.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener)
        binding.bottomNavigation.selectedItemId = currentId
    }

    private fun loadData() {
        // pac进程预热 利用pac进程 爬虫
        PACProcessClient.warmUp()

        // TODO: 设置数据库数据
//        DataDealHelper.deal()

    }

    private fun navigationFragment(): NavigationBarView.OnItemSelectedListener {
        val fragmentManager = supportFragmentManager
        val containerId = R.id.container
        return object : NavigationBarView.OnItemSelectedListener {
            var lastMenu: MenuItem? = null
            var lastTag: String? = null
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                if (lastMenu?.itemId == item.itemId) {
                    return true
                }
                lastMenu = item
                if (AppUtil.isDebug) {
                    Log.d(TAG, "onItemSelectedListener: ${item.title}")
                }
                val tag = when (item.itemId) {
                    R.id.home -> {
                        HomeFragment::class.java.simpleName
                    }

                    R.id.offline -> {
                        OfflineFragment::class.java.simpleName
                    }

                    else -> OldHomeFragment::class.java.simpleName
                }

                UIHelper.switchFragment(lastTag, tag, fragmentManager, containerId) {
                    newFragment(tag)
                }

                lastTag = tag
                currentId = item.itemId
                return true
            }
        }
    }

    private fun newFragment(tag: String): BaseFragment {
        return when (tag) {
            HomeFragment::class.java.simpleName -> {
                HomeFragment()
            }

            OfflineFragment::class.java.simpleName -> {
                OfflineFragment()
            }

            else -> {
                OldHomeFragment()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constant.FIRST_PARAMETER, currentId)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}