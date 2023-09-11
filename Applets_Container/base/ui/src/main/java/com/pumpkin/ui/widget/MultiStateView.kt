package com.pumpkin.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StyleableRes
import com.pumpkin.ui.R

/**
 * pumpkin
 *
 * 多重状态view
 * 状态：内容、加载中、网络错误、其他错误、空、个性化View
 */
class MultiStateView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    FrameLayout(context, attrs, defStyle) {

    /**
     * 状态枚举
     */
    enum class ViewState {
        CONTENT,
        LOADING,
        EMPTY,
        NET_ERROR,
        OTHER_ERROR,
        CUSTOM
    }

    /**
     * 状态监听，改变前
     *
     * @return true 会继续更改state  false 不继续更改状态，状态还是oldState
     */
    private var stateBeforeChangeListener: ((oldState: ViewState, newState: ViewState) -> Boolean)? =
        null

    /**
     * 状态监听，改变后  此时state已经更改为newState
     */
    var stateAfterChangeListener: ((oldState: ViewState, newState: ViewState) -> Unit)? = null

    /**
     * 储存不同状态的view.
     * 根据enum直接定位到内部数组的索引
     */
    private val views = Array<View?>(ViewState.values().size) { null }

    /**
     * 切换动画开关
     */
    var enableAnimateLayoutChanges: Boolean = false

    /**
     * 当前状态设置
     */
    var currentState = ViewState.CONTENT
        set(value) {
            val previewState = field
            if (value != previewState) {
                if (stateBeforeChangeListener?.invoke(previewState, value) != false) {
                    field = value
                    //根据状态进行view显示
                    showViewByState(previewState)

                    stateAfterChangeListener?.invoke(previewState, value)
                }
            }
        }

    init {
        //初始化 配置的布局
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView)
        val layoutInflater = LayoutInflater.from(context)

        //CONTENT布局
        defaultLayoutInflater(
            ViewState.CONTENT,
            R.styleable.MultiStateView_msv_contentView,
            typeArray,
            layoutInflater
        )
        //LOADING布局
        defaultLayoutInflater(
            ViewState.LOADING,
            R.styleable.MultiStateView_msv_loadingView,
            typeArray,
            layoutInflater
        )
        //EMPTY布局
        defaultLayoutInflater(
            ViewState.EMPTY,
            R.styleable.MultiStateView_msv_emptyView,
            typeArray,
            layoutInflater
        )
        //NET_ERROR布局
        defaultLayoutInflater(
            ViewState.NET_ERROR,
            R.styleable.MultiStateView_msv_netErrorView,
            typeArray,
            layoutInflater
        )
        //OTHER_ERROR布局
        defaultLayoutInflater(
            ViewState.OTHER_ERROR,
            R.styleable.MultiStateView_msv_otherErrorView,
            typeArray,
            layoutInflater
        )
        //CUSTOM布局
        defaultLayoutInflater(
            ViewState.CUSTOM,
            R.styleable.MultiStateView_msv_customView,
            typeArray,
            layoutInflater
        )

        //获取并设置默认的viewState 若无默认设置则为 ViewState.CONTENT
        currentState = when (typeArray.getInt(
            R.styleable.MultiStateView_msv_currentViewState,
            ViewState.CONTENT.ordinal
        )) {
            ViewState.CONTENT.ordinal -> ViewState.CONTENT
            ViewState.LOADING.ordinal -> ViewState.LOADING
            ViewState.EMPTY.ordinal -> ViewState.EMPTY
            ViewState.NET_ERROR.ordinal -> ViewState.NET_ERROR
            ViewState.OTHER_ERROR.ordinal -> ViewState.OTHER_ERROR
            ViewState.CUSTOM.ordinal -> ViewState.CUSTOM
            else -> ViewState.CONTENT
        }
        //切换动画开关
        enableAnimateLayoutChanges =
            typeArray.getBoolean(R.styleable.MultiStateView_msv_enableAnimateChanges, false)

        typeArray.recycle()

    }

    /**
     * 添加view通过state
     * @param layoutRes 被添加的layoutRes
     * @param state ViewState
     * @param switchToState 是否需要直接展示
     */
    fun addViewForState(
        @LayoutRes layoutRes: Int,
        state: ViewState,
        switchToState: Boolean = false
    ) {
        val view = LayoutInflater.from(context).inflate(layoutRes, this, false)
        addViewByViewState(view, state, switchToState)
    }

    /**
     * 添加view通过state
     * @param view 被添加的view
     * @param state ViewState
     * @param isImmediatelyShow 是否需要直接展示
     */
    fun addViewByViewState(
        view: View,
        state: ViewState,
        isImmediatelyShow: Boolean = false
    ) {
        //如果之前存在view，且不为null，则先移除
        obtainView(state)?.let {
            removeView(view)
        }
        //持有添加
        views[state.ordinal] = view
        addView(view)
        //如果当前添加的view，需要立即展示，则设置状态即可
        if (isImmediatelyShow) currentState = state else view.visibility = View.GONE
    }

    /**
     * 根据状态获取view
     * @param state ViewsTate
     */
    fun obtainView(state: ViewState): View? = views[state.ordinal]

    /**
     * 根据state显示相关的view
     * @param state View状态
     */
    private fun showViewByState(previousState: ViewState) {
        if (enableAnimateLayoutChanges) {
            //其他View全部隐藏
            for (i in views.indices) {
                if (i != currentState.ordinal && i != previousState.ordinal) {
                    views[i]?.visibility = View.GONE
                }
            }
            //使用animate执行previousView与currentView的切换
            animateView(obtainView(previousState))
            return
        }
        //不执行动画
        for (i in views.indices) {
            if (i == currentState.ordinal) {
                views[i]?.visibility = View.VISIBLE
            } else {
                views[i]?.visibility = View.GONE
            }
        }
    }

    /**
     * 动画执行隐藏显示
     * @param view view
     */
    private fun animateView(previousView: View?) {
        if (previousView == null) {
            obtainView(currentState)?.let { it.visibility = View.VISIBLE }
                ?: throw IllegalStateException("当前状态的view不能为null")
            return
        }
        // TODO: 动画是否需要
        val animateDuration = 200L
        ObjectAnimator.ofFloat(previousView, "alpha", 1.0F, 0.0F).apply {
            duration = animateDuration
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    previousView.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator?) {
                    previousView.visibility = View.GONE
                    obtainView(currentState)?.let {
                        //当前View显示
                        it.visibility = View.VISIBLE
                        ObjectAnimator.ofFloat(it, "alpha", 0.0F, 1.0F)
                            .apply { duration = animateDuration }.start()
                    } ?: throw IllegalStateException("当前状态的view不能为null")
                }
            })
        }.start()
    }

    /**
     * 默认布局填充
     */
    private fun defaultLayoutInflater(
        state: ViewState,
        @StyleableRes res: Int,
        typeArray: TypedArray,
        layoutInflater: LayoutInflater
    ) {
        //view被数组持有  可以利用索引查找
        typeArray.getResourceId(res, View.NO_ID)
            .takeIf { it != View.NO_ID }?.let {
                val contentView = layoutInflater.inflate(it, this, false)
                //不为空，先赋值到数组，并且加入布局
                views[state.ordinal] = contentView
                addView(contentView)
            }
    }

    /**
     * 判断如果不是ViewState中除了contentView以外定义的View，将此view默认设置为contentView
     */
    private fun checkContentView(view: View?) {
        val localView = views
        // 如果contentView为null，且将要add的view不等于其他view，则将其默认赋值给contentview
        if (obtainView(ViewState.CONTENT) == null) {
            var flag = true
            for (index in localView.indices) {
                if (localView[index] == view) {
                    flag = false
                    break
                }
            }
            if (flag) {
                localView[ViewState.CONTENT.ordinal] = view
                if (currentState != ViewState.CONTENT) {
                    obtainView(ViewState.CONTENT)?.visibility = View.GONE
                }
            }
        }


//        second implementation
//        ViewState.values()
//            .superReduce<Pair<Boolean, Boolean>, ViewState> { lastValue, currentViewState ->
//                var isNeedSetContent = lastValue ?: (false to true)
//                if (currentViewState == ViewState.CONTENT && obtainView(currentViewState) == null) {
//                    isNeedSetContent = true to isNeedSetContent.second
//                }
//                if (obtainView(currentViewState) === view) {
//                    isNeedSetContent = isNeedSetContent.first to false
//                }
//                isNeedSetContent
//            }.takeIf { it != null && it.first && it.first == it.second }?.let {
//                views[ViewState.CONTENT.ordinal] = view
//                if (currentState != ViewState.CONTENT) {
//                    obtainView(ViewState.CONTENT)?.visibility = View.GONE
//                }
//            }
    }


    /**
     * 不建议外部直接使用addView进行视图的添加，而应该使用{@link MultiStateView#addViewByViewState}
     */
    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        checkContentView(child)
        super.addView(child, index, params)
    }

    override fun addView(child: View?, width: Int, height: Int) {
        checkContentView(child)
        super.addView(child, width, height)
    }

    override fun addViewInLayout(
        child: View?,
        index: Int,
        params: ViewGroup.LayoutParams?,
        preventRequestLayout: Boolean
    ): Boolean {
        checkContentView(child)
        return super.addViewInLayout(child, index, params, preventRequestLayout)
    }

}



























