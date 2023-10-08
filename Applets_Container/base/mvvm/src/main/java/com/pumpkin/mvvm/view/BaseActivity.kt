package com.pumpkin.mvvm.view

import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean

abstract class BaseActivity : AppCompatActivity() {

    /**
     * 页面设置bean
     */
    private var pageSettingBean: ActivitySettingBean = ActivitySettingBean()


//    fun setStatusHeight(view: View) {
//        //设置状态栏高度
//        val statusBarHeight: Int = DeviceParams.getStatusBarHeight(AppUtil.application)
//        if (AppUtil.isDebug) {
//            "status height is $statusBarHeight .".toLogD(TAG)
//        }
//        val layoutParams = view.layoutParams
//        layoutParams.height = statusBarHeight
//    }

    /**
     * 设置默认的页面bean
     */
    open fun setPageSettings(pageSettingBean: ActivitySettingBean) {
        this.pageSettingBean = pageSettingBean
    }

    override fun finish() {
        super.finish()
        //设置动画
//        overridePendingTransition(pageSettingBean.startEnterAnim, pageSettingBean.startExitAnim)
    }
}