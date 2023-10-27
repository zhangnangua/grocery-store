package com.pumpkin.applets_container.view.fragment

import android.view.View
import com.pumpkin.applets_container.databinding.FragmentWorldBinding
import com.pumpkin.mvvm.view.SuperMultiStateBaseFragment

class WorldFragment : SuperMultiStateBaseFragment() {

    lateinit var binding: FragmentWorldBinding

    override fun initView(view: View) {
        binding = FragmentWorldBinding.inflate(layoutInflater)
        setPACContentView(binding.root)
    }

    override fun loadData() {

    }
}