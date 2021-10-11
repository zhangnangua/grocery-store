package com.zxf.jetpackrelated.databinding

import android.widget.SeekBar
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

/**
 * 作者： zxf
 * 描述： 自定义InverseBindingAdapter 相关
 */


@BindingAdapter(value = ["slideProgress"])
fun setSeekProgress(seekbar: SeekBar, slideProgressOld: Int, slideProgressNew: Int) {
    if (slideProgressOld == slideProgressNew) {
        return
    }
    seekbar.progress = slideProgressNew
}

/**
 *  InverseBindingAdapter 配合  BindingAdapter  一起使用
 *
 * event 非必填, 默认值 属性值 + AttrChanged后缀
 */
@InverseBindingAdapter(attribute = "slideProgress", event = "slideProgressAttrChanged")
fun getSeekProgress(seekbar: SeekBar) = seekbar.progress

@BindingAdapter(value = ["slideProgressAttrChanged"])
fun setSeekProgress(seekbar: SeekBar, inverseBindingListener: InverseBindingListener?) {
    seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            inverseBindingListener?.onChange()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }

    })
}