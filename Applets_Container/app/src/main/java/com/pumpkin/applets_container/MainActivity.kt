package com.pumpkin.applets_container

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.pumpkin.applets_container.databinding.ActivityMain1Binding
import com.pumpkin.applets_container.helper.DataDealHelper
import com.pumpkin.applets_container.view.fragment.MainFragment
import com.pumpkin.applets_container.viewmodel.MainViewModel
import com.pumpkin.mvvm.util.ActivityVB
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import com.pumpkin.pac.process.connectPool.GameConnectPool
import kotlinx.coroutines.launch

/**
 * pumpkin
 *
 * 测试
 */
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMain1Binding

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 启动主题
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMain1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        //主 fragment 设置
        UIHelper.showFragmentNoRemove(
            MainFragment::class.java.simpleName,
            supportFragmentManager,
            MainFragment(),
            R.id.fl_container
        )

        //drawer 设置
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            menuItem.isChecked = true
            binding.drawerLayout.close()
            true
        }

        // 游戏进程预热测试 利用pac进程 爬虫
        GameConnectPool.connect()

        loadData()

        // TODO: 设置数据库数据
        DataDealHelper.deal()
    }

    private fun loadData() {
        lifecycle.coroutineScope.launch {
            // 每次生命周期处于 STARTED 状态（或更高状态）时，repeatOnLifecycle 在新的协程中启动块，并在它停止时取消它。
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getDrawerFlow().collect { isOpen ->
                    if (isOpen.element) {
                        openDrawer()
                    } else {
                        closeDrawer()
                    }
                }
            }
            //如果这里被执行，则代表生命周期已经走到了onDestroy，因为repeatOnLifecycle是挂起函数，在生命周期为onDestroy的时候进行了恢复。

        }
    }

    private fun openDrawer() {
        binding.drawerLayout.open()
    }

    private fun closeDrawer() {
        binding.drawerLayout.close()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}