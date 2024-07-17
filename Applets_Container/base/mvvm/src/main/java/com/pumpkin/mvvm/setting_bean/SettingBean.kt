package com.pumpkin.mvvm.setting_bean

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.AnimRes

/**
 * Activity页面设置bean
 */
class ActivitySettingBean(
    //是否开启沉浸式导航栏，默认关闭
    var enableImmersiveBar: Boolean = false,

    //进入动画  进入   默认从右侧进入
    @AnimRes
    var enterAnim: Int? = null,

    //进入动画  退出   默认从左侧退出
    @AnimRes
    var exitAnim: Int? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (enableImmersiveBar) 1 else 0)
        parcel.writeValue(enterAnim)
        parcel.writeValue(exitAnim)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActivitySettingBean> {
        override fun createFromParcel(parcel: Parcel): ActivitySettingBean {
            return ActivitySettingBean(parcel)
        }

        override fun newArray(size: Int): Array<ActivitySettingBean?> {
            return arrayOfNulls(size)
        }
    }

}

