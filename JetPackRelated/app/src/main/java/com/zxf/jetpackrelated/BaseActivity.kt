package com.zxf.jetpackrelated

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 作者： zxf
 * 描述： 基础的activity
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        title = this::class.java.simpleName
    }

    /**
     * 初始化view操作
     */
    abstract fun initView()
}