package com.zxf.jetpackrelated.ServiceTest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.widget.Toast
import com.zxf.jetpackrelated.BaseActivity

/**
 * 简单service bind测试
 */
class BindServiceSimpleDemoActivity : BaseActivity() {

    private var _service: BindServiceSimpleDemo? = null
    private val service get() = _service!!
    var mBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            _service = (service as BindServiceSimpleDemo.MyBinder).getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

    }

    override fun initView() {

    }

    override fun onStart() {
        super.onStart()
        // bind service
        bindService(
            Intent(this, BindServiceSimpleDemo::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )


        // TODO: 2021/11/4 mock click event , call service function getRandomNumber
        if (mBound) {
            Toast.makeText(
                this,
                "obtain service value is ${service.getRandomNumber()}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
        mBound = false
    }
}