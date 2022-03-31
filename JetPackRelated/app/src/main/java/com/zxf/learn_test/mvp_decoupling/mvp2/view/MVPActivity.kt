package com.zxf.learn_test.mvp_decoupling.mvp2.view

import com.zxf.learn_test.mvp_decoupling.mvp2.map_base.BaseMvpActivity
import com.zxf.learn_test.mvp_decoupling.mvp2.model.MVPModel
import com.zxf.learn_test.mvp_decoupling.mvp2.mvp_interface.Imvp
import com.zxf.learn_test.mvp_decoupling.mvp2.presenter.MVPPresenter

/**
 * mvp2 实现方式  解耦测试
 */
class MVPActivity : BaseMvpActivity<Imvp.IView2, Imvp.IModel2, MVPPresenter>(), Imvp.IView2 {
    override fun createPresenter(): MVPPresenter = MVPPresenter()

    override fun createView(): Imvp.IView2 = this

    override fun createModel(): Imvp.IModel2 = MVPModel()

    override fun myselfFun(str: String) {
        showToast(str)
    }


    override fun onResume() {
        super.onResume()
        presenter?.dealData()
    }
}