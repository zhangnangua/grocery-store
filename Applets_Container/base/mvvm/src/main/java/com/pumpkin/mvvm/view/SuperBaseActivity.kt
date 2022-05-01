package com.pumpkin.mvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean
import com.pumpkin.mvvm.util.AppUtil

/**
 * Activity顶层基类
 * pumpkin
 *
 * todo 统一处理进程被回收之后，则进行重启
 * todo 设置基础布局，title、页面状态
 * todo 设置binding
 * todo 统一进出动画
 */
open class SuperBaseActivity : AppCompatActivity() {

    /**
     * 页面设置bean
     */
    private var _pageSettingBean: ActivitySettingBean? = null
    val pageSettingBean =
        _pageSettingBean ?: throw NullPointerException("ActivitySettingBean 没有初始化或者界面已经被关闭了")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置默认的页面bean
        setPageSettings(ActivitySettingBean())
        // TODO: 2022/4/19 统一处理进行被回收后则进行重启
        if (savedInstanceState != null && AppUtil.isKill) {

        }

        // TODO: 2022/4/19 设置基础布局


        // TODO: 2022/4/21 设置binding
    }

    /**
     * 页面bean设置
     */
    open fun setPageSettings(pageSettingBean: ActivitySettingBean) {
        this._pageSettingBean = pageSettingBean
    }

    override fun finish() {
        super.finish()
        //设置动画
        overridePendingTransition(pageSettingBean.startEnterAnim, pageSettingBean.startExitAnim)
    }

}