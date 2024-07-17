//package com.pumpkin.ui.widget
//
//import android.content.Context
//import android.util.AttributeSet
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import com.pumpkin.data.AppUtil
//import com.pumpkin.ui.R
//
//class FlowGridContainer
//@JvmOverloads
//constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : ViewGroup(context, attrs, defStyle) {
//    private val rawSpacing: Int
//    private val columnSpacing: Int
//    private val gridCount: Int
//
//    init {
//        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.FlowGridContainer)
//        rawSpacing = typeArray.getDimensionPixelOffset(R.styleable.FlowGridContainer_row_spacing, 0)
//        columnSpacing = typeArray.getDimensionPixelOffset(R.styleable.FlowGridContainer_column_spacing, 0)
//        gridCount = typeArray.getDimensionPixelOffset(R.styleable.FlowGridContainer_grid_count, 2)
//        typeArray.recycle()
//    }
//
//    /**
//     * 在视图大小发生变化时调用，包括在布局膨胀时第一次绘制。
//     * 覆盖 onSizeChanged() 以计算与自定义视图大小相关的位置、尺寸和任何其他值，而不是在每次绘制时重新计算它们。
//     */
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        val childCount = childCount
//        for (index in 0 until childCount) {
//            val view = getChildAt(index) ?: continue
//            if (view.visibility == View.GONE) {
//                continue;
//            }
//            val layoutParams = view.layoutParams
//            if (layoutParams == null) {
//                if (AppUtil.isDebug) {
//                    Log.d(TAG, "onMeasure () -> view layoutParams is null")
//                }
//                continue
//            }
//            val childWidthSpec = getChildMeasureSpec(widthMeasureSpec, paddingStart + paddingEnd, layoutParams.width)
//            val childHeightSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, layoutParams.height)
//            view.measure(childWidthSpec, childHeightSpec)
//            val measuredHeight = view.measuredHeight
//            val measuredWidth = view.measuredWidth
//
////            resolveSize()
//        }
//    }
//
//    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
////        val v : View;
////        v.la
//    }
//
//    companion object {
//        val TAG = "FlowContainer"
//    }
//}