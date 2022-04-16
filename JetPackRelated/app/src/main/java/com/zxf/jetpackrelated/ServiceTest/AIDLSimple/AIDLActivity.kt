package com.zxf.jetpackrelated.ServiceTest.AIDLSimple

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.zxf.jetpackrelated.IMyAidlInterface


class AIDLActivity : AppCompatActivity() {

    private var aidlInterface: IMyAidlInterface? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            //它会试着查找Binder本地对象，如果找到，说明Client和Server都在同一个进程，这个参数直接就是本地对象，直接强制类型转换然后返回，
            //如果找不到，说明是远程对象（处于另外一个进程）那么就需要创建一个Binde代理对象，让这个Binder代理实现对于远程对象的访问
            aidlInterface = IMyAidlInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    override fun onStart() {
        super.onStart()
        //尝试建立链接
        bindService(
            Intent(this, AIDLServiceSimpleDemo::class.java)
                .also {
                    it.action = "com.zxf.aidl"
                    it.setPackage("com.zxf.jetpackrelated")
                },
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )

        // TODO: 2021/11/5 模拟获取aidl的信息
        val name = aidlInterface?.name
        print(name)
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }
}