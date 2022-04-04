package com.zxf.learn_test

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.zxf.jetpackrelated.BaseActivity
import com.zxf.jetpackrelated.R
import com.zxf.jetpackrelated.databinding.ActivityLeanTestActivityBinding
import com.zxf.jetpackrelated.utils.*
import com.zxf.learn_test.LruUtil.LruTestActivity

/**
 *
 *
 * todo A启动B 生命周期回调  A onPause B onCreate 、 onStart 、 onResume A onStop  返回 B onPause A onStart 、 onResume B onStop 、 onDestroy
 * todo A启动B(设置主题包含，windowIsTranslucent = true. 允许Activity背景透明，否则强制黑色填充)   A onPause B onCreate 、 onStart 、 onResume  返回 B onPause A onResume B onStop 、 onDestroy
 *
 * todo 关于dialog弹出后Activity生命周期说明 由于用同一个WindowManager，所以Activity的生命周期不会产生变化，即不会调用onPause。除非是别的Activity的dialog弹出，半遮挡到当前的Activity，才会有生命周期的变化
 */
class LearnTestActivity : BaseActivity() {

    lateinit var binding: ActivityLeanTestActivityBinding

    override fun initView() {

        //尝试调整Activity的高度  todo 点击等相关事件的传递
        val window = window
        val lp: WindowManager.LayoutParams = window.attributes
        lp.height = (obtainPhoneCurrentHeight(this) * 0.8).toInt()
        lp.gravity = Gravity.BOTTOM
        window.attributes = lp

        //布局加载
        binding = ActivityLeanTestActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //生命周期事件监听
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                "LearnTestActivity${event.name}".toLogI()
            }
        })

        //dialog 弹窗
        binding.btDialog.setOnClickListener {
            // TODO: 2022/3/20 Dialog 使用测试
            // todo 关于dialog弹出后Activity生命周期说明 由于用同一个WindowManager，所以Activity的生命周期不会产生变化
            val dialog = object : AppCompatDialog(this) {
                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    setContentView(TextView(this@LearnTestActivity).apply {
                        text = "dialog simple test"
                    })
                }
            }

            dialog.show()

            //隐藏title
            window?.decorView?.findViewById<View>(R.id.title)?.visibility = View.GONE
            //todo dialog调用show方法才会执行onCreate方法去填充decorView，只有DecorView不为null的情况下，下述的设置才会有效果，因为Dialog 的onWindowAttributesChanged方法，只有在DecorView不为null的情况下才会进行属性设置
            val phoneWindow = dialog.window
            if (phoneWindow != null) {
                val wlp = phoneWindow.attributes
                wlp.gravity = Gravity.CENTER
                wlp.height = (obtainPhoneCurrentHeight(this@LearnTestActivity) * 0.5).toInt()
                wlp.width = (obtainPhoneCurrentWidth(this@LearnTestActivity) * 0.5).toInt()

                phoneWindow.attributes = wlp
            }

        }

        //toast 弹窗
        binding.btToast.setOnClickListener {
            ToastUtil.toastShort("主线程toast！！！")
            Thread {
                //异步toast
                ToastUtil.toastShort("异步线程toast！！！线程名字：${Thread.currentThread().name}")
            }.start()
        }

        /**
         * lru 测试界面
         */
        binding.btLruTestActivity.setOnClickListener {
            startActivity(Intent(this, LruTestActivity::class.java))
        }

    }

    /**
     * todo 事件传递给其他Activity测试
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        getActivityThread()
//        MainActivity.instanceTest!!.dispatchTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

}