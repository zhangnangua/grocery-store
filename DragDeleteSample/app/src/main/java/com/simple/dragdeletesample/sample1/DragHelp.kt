package com.simple.dragdeletesample.sample1

import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.drawToBitmap

/**
 * 作者： zxf
 * 描述： 描述
 */
class MyDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {

//    private val shadow: Drawable

    init {
//        view.visibility = View.INVISIBLE
        //将view  转换为drawable
//        shadow = BitmapDrawable(null, view.drawToBitmap())
    }

    /**
     * 用于设置拖动阴影的大小和触摸点位置
     */
    override fun onProvideShadowMetrics(outShadowSize: Point?, outShadowTouchPoint: Point?) {
        super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint)
//        val width = view.width
//        val height = view.height
//        //设置阴影的大小
//        shadow.setBounds(0, 0, width, height)
//        //设置长宽值，通过outShadowSize 返回给系统
//        outShadowSize?.set(width, height)
//        //设置触摸点的位置，为拖动阴影的中心
//        outShadowTouchPoint?.set(width / 2, height / 2)
    }

    /**
     * 拖动阴影的绘制
     */
    override fun onDrawShadow(canvas: Canvas?) {
        super.onDrawShadow(canvas)
//        shadow.draw(canvas!!)
    }
}
