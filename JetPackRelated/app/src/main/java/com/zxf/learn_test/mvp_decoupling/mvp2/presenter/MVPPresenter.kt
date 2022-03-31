package com.zxf.learn_test.mvp_decoupling.mvp2.presenter

import com.zxf.learn_test.mvp_decoupling.mvp2.map_base.BasePresenter
import com.zxf.learn_test.mvp_decoupling.mvp2.mvp_interface.Imvp

/**
 * mvp2 实现方式  解耦测试
 */
class MVPPresenter : BasePresenter<Imvp.IView2, Imvp.IModel2>() {

    fun dealData() {
        view?.myselfFun(model?.obtainData().toString())
    }

    override fun onDestroy() {

    }
}