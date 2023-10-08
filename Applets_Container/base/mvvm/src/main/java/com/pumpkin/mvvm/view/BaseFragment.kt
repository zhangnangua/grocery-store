package com.pumpkin.mvvm.view

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private var initData = false


    override fun onDestroyView() {
        super.onDestroyView()
        initData = false
    }

    override fun onResume() {
        super.onResume()
        if (!initData) {
            initData = true
            loadData()
        }
    }

    abstract fun loadData()
}