package com.zxf.jetpackrelated.liveData.share_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.zxf.jetpackrelated.databinding.FragmentSeekbarBinding

/**
 * 作者： zxf
 * 描述： fragment 数据共享 fragment
 */

class SeekBarFragment
@JvmOverloads
constructor(private val text: String = "") : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return FragmentSeekbarBinding.inflate(layoutInflater).let {
            fragmentSeekBarBinding ->

            fragmentSeekBarBinding.tvContent.text = this.text

            //注意这里使用activity，因为如果想共享数据需要使用同一个ViewModelStore。所以都是用activity的。
            val liveDataProgress = ViewModelProvider(activity!!)[ShareDataViewModel::class.java].progress

            //通过observe观察ViewModel中字段数据的变化，并在变化时得到通知
            liveDataProgress.observe(this){
                progress->
                fragmentSeekBarBinding.seekbar.progress = progress
            }

            fragmentSeekBarBinding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    //当用户操作SeekBar时，更新ViewModel中的数据
                    liveDataProgress.value = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })

            fragmentSeekBarBinding.root
        }

    }

}