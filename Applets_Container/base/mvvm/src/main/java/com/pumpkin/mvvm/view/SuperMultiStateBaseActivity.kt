package com.pumpkin.mvvm.view

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.databinding.BaseLayoutBinding
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean
import com.pumpkin.ui.widget.MultiStateView

/**
 * Activity顶层基类
 * pumpkin
 *
 * todo 统一处理进程被回收之后，则进行重启
 * todo 设置基础布局、title、页面状态
 * todo 设置binding
 * todo 统一进出动画
 * todo 埋点
 */
open class SuperMultiStateBaseActivity : BaseActivity() {

    private lateinit var superBinding: BaseLayoutBinding

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
        val multiplyState = superBinding.root
        multiplyState.addViewForState(layoutRes, state, switchToState)
    }

    /**
     * customize basic relate layout settings.
     */
    open fun setBaseContentForState(
        view: View,
        state: MultiStateView.ViewState,
        switchToState: Boolean = false
    ) {
        val multiplyState = superBinding.root
        multiplyState.addViewByViewState(view, state, switchToState)
    }

    /**
     * set current state
     */
    fun switchState(state: MultiStateView.ViewState) {
        superBinding.root.currentState = state
    }

    /**
     * obtain view by state
     */
    fun getViewByState(state: MultiStateView.ViewState) =
        superBinding.root.obtainView(state)

    fun setPACContentView(v: View) {
        setBaseContentForState(v, MultiStateView.ViewState.CONTENT, true)
    }

    companion object {
        private const val TAG = "superBase"
    }

}