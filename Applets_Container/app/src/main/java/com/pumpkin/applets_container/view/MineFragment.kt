package com.pumpkin.applets_container.view

import android.view.View
import com.pumpkin.applets_container.databinding.FragmentMineBinding
import com.pumpkin.mvvm.view.SuperMultiStateBaseFragment

class MineFragment : SuperMultiStateBaseFragment() {

    lateinit var binding: FragmentMineBinding

    override fun initView(view: View) {
        binding = FragmentMineBinding.inflate(layoutInflater)
        setPACContentView(binding.root)
    }

    override fun loadData() {

    }
}