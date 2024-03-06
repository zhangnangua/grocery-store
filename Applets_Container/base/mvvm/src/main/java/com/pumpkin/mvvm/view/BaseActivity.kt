package com.pumpkin.mvvm.view

import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.AnimRes
import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.mvvm.R
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.StatusBarUtil

abstract class BaseActivity : AppCompatActivity() {
    /**
     * 页面设置bean
     */
    private var pageSettingBean: ActivitySettingBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle: Bundle? = savedInstanceState ?: intent.extras
        if (bundle != null) {
            val localPage: ActivitySettingBean? = bundle.getParcelable(Constant.PAGE_PARAMETER)
            pageSettingBean = localPage
            enterAnim(localPage)
        }
        if (pageSettingBean?.enableImmersiveBar == true) {
            StatusBarUtil.hideBar(window)
            StatusBarUtil.fullScreenAndLayoutStable(window)
        }
        super.onCreate(savedInstanceState)
    }


    /**
     * 设置默认的页面bean
     */
    open fun setPageSettings(pageSettingBean: ActivitySettingBean) {
        this.pageSettingBean = pageSettingBean
    }

    override fun finish() {
        super.finish()
        exitAnim(pageSettingBean)
    }

    private fun exitAnim(pageSettingBean: ActivitySettingBean?) {
        val localPage = pageSettingBean

        @AnimRes
        var exitAnim: Int? = null
        if (localPage != null) {
            exitAnim = localPage.exitAnim
        }
        if (exitAnim != null) {
            overridePendingTransition(R.anim.slide_no_process, exitAnim)
        }
    }

    private fun enterAnim(pageSettingBean: ActivitySettingBean?) {
        val localPage = pageSettingBean

        @AnimRes
        var enterAnim: Int? = null
        if (localPage != null) {
            enterAnim = localPage.enterAnim
        }
        if (enterAnim != null) {
            overridePendingTransition(enterAnim, R.anim.slide_no_process)
        }
    }
}