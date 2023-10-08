package com.pumpkin.mvvm.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.view.BaseFragment

object UIHelper {

    /**
     * showFragment
     */
    fun showFragmentNoRemove(
        tag: String?,
        fragmentManager: FragmentManager,
        newFragment: Fragment,
        containerId: Int
    ) {
        try {
            var oldFragment: Fragment? = null
            val transaction = fragmentManager.beginTransaction()
            if (tag != null) {
                oldFragment = fragmentManager.findFragmentByTag(tag)
            }
            if (oldFragment != null) {
                transaction.setMaxLifecycle(oldFragment, Lifecycle.State.RESUMED)
                transaction.show(oldFragment)
            } else {
                transaction.add(containerId, newFragment, tag)
                transaction.commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            if (AppUtil.isDebug) {
                throw e
            }
        }
    }

    /**
     * switchFragment
     */
    inline fun switchFragment(
        lastTag: String?,
        newTag: String,
        fragmentManager: FragmentManager,
        containerId: Int,
        newFragment: () -> BaseFragment
    ) {
        try {
            var lastFragment: Fragment? = null
            val transaction = fragmentManager.beginTransaction()
            if (lastTag != null) {
                lastFragment = fragmentManager.findFragmentByTag(lastTag)
            }
            if (lastFragment != null) {
                transaction.setMaxLifecycle(lastFragment, Lifecycle.State.STARTED)
                    .hide(lastFragment)
            } else {
                // bug fix 系统语言切换之后丢失oldTag 导致另一个Fragment无法隐藏
                // 尝试遍历存在的fragment 全部进行隐藏
                for (fragment in fragmentManager.fragments) {
                    if (fragment != null) {
                        transaction.setMaxLifecycle(fragment, Lifecycle.State.STARTED)
                            .hide(fragment)
                    }
                }
            }
            val oldFragment: Fragment? = fragmentManager.findFragmentByTag(newTag)
            if (oldFragment == null) {
                transaction.add(containerId, newFragment.invoke(), newTag)
            } else {
                transaction.setMaxLifecycle(oldFragment, Lifecycle.State.RESUMED)
                transaction.show(oldFragment)
            }
            transaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            if (AppUtil.isDebug) {
                throw e
            }
        }
    }

}