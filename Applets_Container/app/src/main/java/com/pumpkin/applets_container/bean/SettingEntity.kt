package com.pumpkin.applets_container.bean

import androidx.annotation.StringRes

class SettingEntity(@StringRes val title: Int, val icon: Int, val introduction: String? = null, val marginTop: Int = 0)