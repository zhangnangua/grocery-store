package com.simple.csdndemo.head

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.simple.csdndemo.R
import kotlin.math.absoluteValue


/**
 * 作者： zxf
 * 描述： HeadImageView 帮助工具集合 协助处理注册背景图等
 */

//获取要显示在image上的字符串
typealias ObtainImageFontText = (name: String) -> String

//获取要显示在image上的对应的背景色
typealias ObtainImageFontBackgroundColorInt = (name: String) -> Int

object HeadImageViewHelp {

    /**
     * 如果传入了定制化的处理逻辑 使用该逻辑返回对应的text
     */
    private var obtainImageFontText: ObtainImageFontText? = null

    /**
     * 如果传入了定制化的处理逻辑 使用该逻辑返回对应的background
     */
    private var obtainImageFontBackgroundColorInt: ObtainImageFontBackgroundColorInt? = null

    /**
     * 如果传入了Drawable，则所有的头像默认背景图为该Drawable
     */
    var obtainDefaultDrawable: Drawable? = null

    /**
     * 统一 动画执行时长 默认250
     */
    var animatorTime: Long = 250

    /**
     * 统一 是否启用动画
     */
    var enableAnim = true

    /**
     * 注册obtainImageFontText
     */
    @JvmStatic
    fun registerObtainImageFont(obtainImageFontText: ObtainImageFontText) {
        HeadImageViewHelp.obtainImageFontText = obtainImageFontText
    }

    /**
     * 注册obtainImageFontBackgroundColorIntAndFontText
     */
    @JvmStatic
    fun registerObtainImageFontBackgroundColorInt(obtainImageFontBackgroundColorInt: ObtainImageFontBackgroundColorInt) {
        HeadImageViewHelp.obtainImageFontBackgroundColorInt = obtainImageFontBackgroundColorInt
    }

    /**
     * 获取显示的text
     */
    @JvmStatic
    fun obtainImageFontText(name: String): String {
        if (obtainImageFontText == null) {
            registerObtainImageFont(HeadImageViewHelp::defaultObtainImageFontText)
        }
        return obtainImageFontText!!.invoke(name)
    }

    /**
     * 获取显示的BackgroundColor
     */
    @JvmStatic
    fun obtainImageFontBackgroundColorInt(name: String): Int {
        if (obtainImageFontBackgroundColorInt == null) {
            registerObtainImageFontBackgroundColorInt(HeadImageViewHelp::defaultObtainImageFontBackgroundColorInt)
        }
        return obtainImageFontBackgroundColorInt!!.invoke(name)
    }

    /**
     * 默认获取imageFontText逻辑
     */
    private fun defaultObtainImageFontText(name: String): String {
        val length = name.length
        return if (length > 2) {
            name.substring(length - 2, length)
        } else {
            name
        }
    }

    /**
     * 默认获取imageBackground逻辑
     */
    private fun defaultObtainImageFontBackgroundColorInt(name: String): Int {
        val bgColors = intArrayOf(
            Color.parseColor("#FA7976"),
            Color.parseColor("#B7A0F1"),
            Color.parseColor("#6890F3"),
            Color.parseColor("#57BAB3"),
            Color.parseColor("#61C7F1"),
            Color.parseColor("#FAA77D")
        )

        return name.toByteArray().fold(0) { acc: Int, byte: Byte ->
            acc + byte
        }.let {
            bgColors[it.absoluteValue % bgColors.size]
        }
    }
}

//region  处理glide加载相关

/**
 * 加载图片
 */
fun HeadImageView.loadImage(url: String, name: String) {
    val text = HeadImageViewHelp.obtainImageFontText(name)
    val colorInt = HeadImageViewHelp.obtainImageFontBackgroundColorInt(name)

    val tag = getTag(R.id.imageload_tag)
    if (tag == null || !TextUtils.equals(tag.toString(), url)) {
        //显示默认图片
        setBackgroundFontColorAndText(colorInt, text)
    }

    Glide.with(this).load(url).into(ImageViewTarget(this))
}

/**
 * 自定义ImageViewTarget设置图片
 */
class ImageViewTarget(ivPic: HeadImageView) : CustomViewTarget<HeadImageView, Drawable>(ivPic) {
    override fun onLoadFailed(errorDrawable: Drawable?) {
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        this.view.setImageDrawable(resource)
        //默认取消文字显示
        this.view.cancelDefaultPic()
    }

    override fun onResourceCleared(placeholder: Drawable?) {
    }

}

//endregion
