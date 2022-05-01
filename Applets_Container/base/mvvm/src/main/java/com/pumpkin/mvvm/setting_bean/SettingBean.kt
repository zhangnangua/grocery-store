package com.pumpkin.mvvm.setting_bean

import androidx.annotation.AnimRes
import com.pumpkin.mvvm.R

/**
 * Activity/Fragment 通用设置bean
 */
open class CommonSettingBean(
    //是否使用顶部导航栏
    var enableTopBar: Boolean,

    //状态栏字体颜色是否为黑色
    var enableStatusBarDarkFont: Boolean,
)

/**
 * Activity页面设置bean
 */
class ActivitySettingBean(
    //是否开启沉浸式导航栏，默认开启
    var enableImmersiveBar: Boolean = true,

    //进入动画  进入   默认从右侧进入
    @AnimRes
    var startEnterAnim: Int = R.anim.slide_in_from_right,

    //进入动画  退出   默认从左侧退出
    @AnimRes
    var startExitAnim: Int = R.anim.slide_out_to_left
) : CommonSettingBean(
    //默认不使用顶部状导航栏
    enableTopBar = false,
    //默认状态栏字体为黑色
    enableStatusBarDarkFont = true
)

/**
 * Fragment页面设置Bean
 */
class FragmentSettingBean(

) : CommonSettingBean(
    //默认使用顶部导航栏
    enableTopBar = true,
    //默认字体为黑色
    enableStatusBarDarkFont = true
)

