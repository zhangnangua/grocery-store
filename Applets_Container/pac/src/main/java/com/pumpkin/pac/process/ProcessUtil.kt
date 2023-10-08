package com.pumpkin.pac.process

import android.os.Process
import android.text.TextUtils
import com.pumpkin.data.AppUtil

object ProcessUtil {

    fun isMainProcess() = TextUtils.equals(AppUtil.obtainProcessName(), AppUtil.application.packageName)

    fun isPACProcess() = TextUtils.equals(AppUtil.obtainProcessName(), obtainPACProcessName())

    fun obtainPACProcessName() = "${AppUtil.application.packageName}:pac"

    fun pId() = Process.myPid()
}