package com.pumpkin.applets_container.view

import android.view.View
import com.pumpkin.applets_container.databinding.FragmentOfflineBinding
import com.pumpkin.mvvm.view.SuperMultiStateBaseFragment

class OfflineFragment : SuperMultiStateBaseFragment() {

    lateinit var binding: FragmentOfflineBinding

    override fun initView(view: View) {
        binding = FragmentOfflineBinding.inflate(layoutInflater)
        setPACContentView(binding.root)
    }

    override fun loadData() {

    }
}