package com.pumpkin.applets_container.view.fragment

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.pumpkin.applets_container.databinding.FragmentWorldBinding
import com.pumpkin.mvvm.view.SuperMultiStateBaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WorldFragment : SuperMultiStateBaseFragment() {

    lateinit var binding: FragmentWorldBinding

    override fun initView(view: View) {
        binding = FragmentWorldBinding.inflate(layoutInflater)
        setPACContentView(binding.root)

//        lifecycleScope.launch{
//            delay(5000)
//            binding.loading.visibility = View.GONE
//            delay(5000)
//            binding.loading.visibility = View.VISIBLE
//        }
    }

    override fun loadData() {

    }
}