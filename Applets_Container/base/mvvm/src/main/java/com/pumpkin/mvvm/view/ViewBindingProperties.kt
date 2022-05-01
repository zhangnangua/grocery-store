package com.pumpkin.mvvm.util

import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * 作者:  pumpkin
 * 描述:  viewBinding委托代理生成
 */

internal class ActivityVB<in A : AppCompatActivity, out V : ViewBinding>(
    private val viewBinder: (A) -> V
) : ReadOnlyProperty<A, V> {
    private var binding: V? = null

    /**
     * 在真正使用的时候，才会第一次调用。
     */
    @MainThread
    override fun getValue(thisRef: A, property: KProperty<*>): V {
        //已经绑定过了，则直接返回
        if (binding != null) {
            return binding!!
        }

        //获取到binding
        val viewBinding = viewBinder(thisRef)

        val lifecycle = thisRef.lifecycle
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            "lifecycle的生命周期为destroyed，且localBinding尚未创建，此时不进行binding的缓存。".toLogI(thisRef::class.java.simpleName)
        } else {
            //缓存viewBinding
            binding = viewBinding
            //利用lifecycle，来自动执行解绑，防止内存泄漏
            lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        //释放
                        release()
                        //移除监听
                        source.lifecycle.removeObserver(this)
                    }
                }
            })
        }
        return viewBinding
    }

    /**
     * 释放
     */
    private fun release() {
        binding = null
    }

}

internal class FragmentVB {

}