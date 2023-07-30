package com.pumpkin.pac.process

import android.os.Process
import android.text.TextUtils
import com.pumpkin.ui.util.AppUtil

object ProcessUtil {
    fun isPACProcess() = TextUtils.equals(AppUtil.obtainProcessName(), obtainPACProcessName())

    fun obtainPACProcessName() = "${AppUtil.application.packageName}:pac"

    fun pId() = Process.myPid()
}