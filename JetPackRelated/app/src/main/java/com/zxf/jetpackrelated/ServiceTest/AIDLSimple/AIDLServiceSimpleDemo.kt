package com.zxf.jetpackrelated.ServiceTest.AIDLSimple

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.zxf.jetpackrelated.IMyAidlInterface

class AIDLServiceSimpleDemo : Service() {

    private val iMyAidlInterface = object : IMyAidlInterface.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {

        }

        override fun getName(): String {
            return "zxf"
        }

    }


    override fun onBind(intent: Intent?): IBinder {
        return iMyAidlInterface
    }


}