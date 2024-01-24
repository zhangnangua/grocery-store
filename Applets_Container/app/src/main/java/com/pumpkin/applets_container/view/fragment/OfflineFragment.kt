package com.pumpkin.applets_container.view.fragment

import android.view.View
import com.pumpkin.applets_container.databinding.FragmentOfflineBinding
import com.pumpkin.game.NativeEntrance
import com.pumpkin.mvvm.view.SuperMultiStateBaseFragment

class OfflineFragment : SuperMultiStateBaseFragment() {

    lateinit var binding: FragmentOfflineBinding

    override fun initView(view: View) {
        binding = FragmentOfflineBinding.inflate(layoutInflater)
        setPACContentView(binding.root)

        binding.tBt.setOnClickListener {
//            activity?.startActivity(Intent(activity, AndroidInvaders::class.java))
            NativeEntrance().openQuiz(requireActivity())
        }
    }

    override fun loadData() {

    }
}