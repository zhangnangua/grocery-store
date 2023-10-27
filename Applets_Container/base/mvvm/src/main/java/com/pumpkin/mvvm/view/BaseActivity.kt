package com.pumpkin.mvvm.view

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.ui.util.DeviceParams

abstract class BaseActivity : AppCompatActivity() {

    /**
     * 页面设置bean
     */
    private var pageSettingBean: ActivitySettingBean = ActivitySettingBean()

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