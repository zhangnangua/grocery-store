package com.pumpkin.applets_container.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.navigation.NavigationBarView
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.databinding.FragmentMainBinding
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.view.BaseFragment


class MainFragment : BaseFragment() {

    var currentId = R.id.home

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val onItemSelectedListener = navigationFragment()

        currentId = savedInstanceState?.getInt(Constant.FIRST_PARAMETER) ?: R.id.home

        return FragmentMainBinding.inflate(layoutInflater).let {
            //底部导航栏
            it.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener)
            it.bottomNavigation.selectedItemId = currentId

            it.root
        }
    }

    override fun loadData() {
        
    }

    private fun navigationFragment(): NavigationBarView.OnItemSelectedListener {
        val fragmentManager = childFragmentManager
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
                    else -> WorldFragment::class.java.simpleName
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
                WorldFragment()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constant.FIRST_PARAMETER, currentId)
    }

    companion object {
        private const val TAG = "MainFragment"
    }

}