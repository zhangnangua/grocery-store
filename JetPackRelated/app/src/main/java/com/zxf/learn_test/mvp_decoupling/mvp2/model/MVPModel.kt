package com.zxf.learn_test.mvp_decoupling.mvp2.model

import com.zxf.learn_test.mvp_decoupling.mvp2.mvp_interface.Imvp

/**
 * mvp2 实现方式  解耦测试
 */
class MVPModel : Imvp.IModel2{
    override fun obtainData(): Int = 0
}