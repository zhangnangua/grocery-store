package com.pumpkin.applets_container.view

import android.view.View
import com.pumpkin.applets_container.databinding.FragmentHomeBinding
import com.pumpkin.mvvm.view.SuperMultiStateBaseFragment

class HomeFragment : SuperMultiStateBaseFragment() {

    lateinit var binding: FragmentHomeBinding

    override fun initView(view: View) {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setPACContentView(binding.root)
    }

    override fun loadData() {

    }
}