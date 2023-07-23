package com.pumpkin.mvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.pumpkin.mvvm.R
import com.pumpkin.mvvm.databinding.BaseLayoutBinding
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean
import com.pumpkin.ui.util.AppUtil
import com.pumpkin.ui.widget.MultiStateView

/**
 * Activity顶层基类
 * pumpkin
 *
 * todo 统一处理进程被回收之后，则进行重启
 * todo 设置基础布局，title、页面状态
 * todo 设置binding
 * todo 统一进出动画
 * todo 埋点
 */
open class SuperBaseActivity : AppCompatActivity() {

    protected lateinit var superBinding: BaseLayoutBinding

    /**
     * 页面设置bean
     */
    private var pageSettingBean: ActivitySettingBean = ActivitySettingBean()

    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateBefore(savedInstanceState)
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null && AppUtil.isKill) {
            // TODO: 2022/4/19 统一处理进行被回收后则进行重启

        }
        superBinding = BaseLayoutBinding.inflate(layoutInflater)
        // 设置基础布局
        setContentView(superBinding.root)

        onCreateAfter(savedInstanceState)
    }

    open fun onCreateBefore(savedInstanceState: Bundle?) {}
    open fun onCreateAfter(savedInstanceState: Bundle?) {}

    /**
     * customize basic relate layout settings.
     */
    open fun setBaseContentForState(
        @LayoutRes layoutRes: Int,
        state: MultiStateView.ViewState,
        switchToState: Boolean = false
    ) {
        val multiplyState = superBinding.multiplyState
        multiplyState.addViewForState(layoutRes, state, switchToState)
    }

    fun setRootState(state: MultiStateView.ViewState) {
        superBinding.multiplyState.currentState = state
    }

    /**
     * basic related layout settings.
     */
    fun setPACContentView(@LayoutRes layoutResID: Int) {
        val container = findViewById<FrameLayout>(R.id.pac_container)
        container.removeAllViews()
        LayoutInflater.from(this).inflate(layoutResID, container)
    }

    fun setPACContentView(v: View) {
        val container = findViewById<FrameLayout>(R.id.pac_container)
        container.removeAllViews()
        container.addView(v)
    }

    /**
     * 设置默认的页面bean
     */
    open fun setPageSettings(pageSettingBean: ActivitySettingBean) {
        this.pageSettingBean = pageSettingBean
    }

    override fun finish() {
        super.finish()
        //设置动画
        overridePendingTransition(pageSettingBean.startEnterAnim, pageSettingBean.startExitAnim)
    }

}