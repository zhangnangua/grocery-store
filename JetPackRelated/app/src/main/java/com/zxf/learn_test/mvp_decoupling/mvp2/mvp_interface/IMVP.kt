package com.zxf.learn_test.mvp_decoupling.mvp2.mvp_interface

import com.zxf.learn_test.mvp_decoupling.mvp2.map_base.Model
import com.zxf.learn_test.mvp_decoupling.mvp2.map_base.View

interface Imvp {
    interface IView2 : View {
        fun myselfFun(str: String)
    }

    interface IModel2 : Model {
        fun obtainData(): Int
    }
}