package com.simple.demo.head

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import com.itheima.roundedimageview.RoundedImageView
import com.simple.demo.R
import com.simple.demo.util.AppUtil
import com.simple.demo.util.spToPx
import java.lang.ref.SoftReference

/**
 * 作者： zxf
 * 描述： 头像ImageView
 */
class HeadImageView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    RoundedImageView(context, attrs, defStyle) {

    private val mPaint: Paint = Paint()

    /**
     * 显示的字体颜色默认为白色
     */
    @ColorInt
    var fontColor: Int = -1
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 显示的字体的大小默认为16sp
     */
    @Px
    var fontSize: Float = 0.0F
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 是否使用默认的图片  即背景色加上字体
     */
    var isDefaultPic: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 显示文字时候的背景色  默认显示 #2e6be5
     */
    @ColorInt
    private var backgroundFontColorInt: Int = -1

    /**
     * 配合属性动画处理image默认的转场动画  值在 0-1 之间
     */
    var animationControl: Float = 1F
        set(value) {
            field = if (animationControl < 0) 0F else if (animationControl > 1) 1F else value
            invalidate()
        }

    /**
     * 显示的文字
     */
    private var displayText: String = ""

    /**
     * 可设置默认drawable，若设置了drawable则不显示原本的背景色+文字策略，直接显示Drawable
     */
    private var mBackgroundDrawable: Drawable? = HeadImageViewHelp.obtainDefaultDrawable

    /**
     * 动画持有
     */
    private var objectAnimatorSoft: SoftReference<ObjectAnimator?>? = null

    /**
     * 动画执行时长
     */
    var animatorTime: Long = HeadImageViewHelp.animatorTime

    /**
     * 是否启用动画
     */
    var enableAnim = HeadImageViewHelp.enableAnim

    init {
        //解析xml属性
        val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.HeadImageView)

        fontColor = attributeSet.getColor(
            R.styleable.HeadImageView_font_color,
            ContextCompat.getColor(context, R.color.white)
        )
        fontSize =
            attributeSet.getDimension(R.styleable.HeadImageView_font_size, 16F.spToPx)
        backgroundFontColorInt = attributeSet.getColor(
            R.styleable.HeadImageView_background_font_color,
            Color.parseColor("#2E6BE5")
        )
        isDefaultPic = attributeSet.getBoolean(R.styleable.HeadImageView_is_default_pic, false)

        attributeSet.recycle()
    }

    init {
        //初始化
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
    }

    init {
        //初始化  取消显示defaultPic 动画
        if (enableAnim) {
            if (objectAnimatorSoft == null) {
                objectAnimatorSoft = SoftReference(ObjectAnimator.ofFloat(this, "animationControl", 1F, 0F))
            }
            objectAnimatorSoft?.get()?.let {
                it.duration = animatorTime
                it.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        this@HeadImageView?.isDefaultPic = false
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        this@HeadImageView?.isDefaultPic = false
                    }

                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                })
            }
        }
    }

    /**
     * 设置文字的背景以及文字
     */
    fun setBackgroundFontColorAndText(
        @ColorInt backgroundColor: Int,
        @NonNull displayText: String
    ) {
        if (enableAnim) {
            //取消动画操作
            objectAnimatorSoft?.get()?.cancel()
        }
        this.backgroundFontColorInt = backgroundColor
        this.displayText = displayText
        this.isDefaultPic = true
        this.animationControl = 1F
    }

    /**
     * 取消默认图片显示(含动画效果)
     */
    fun cancelDefaultPic() {
        if (isDefaultPic) {
            //如果绘制的是drawable则没有动画效果，直接设置isDefaultPic重新绘制即可
            if (mBackgroundDrawable != null) {
                isDefaultPic = false
                return
            }
            if (enableAnim) {
                //启用动画
                objectAnimatorSoft?.get()?.start()
            } else {
                //不启用动画
                isDefaultPic = false
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        customOnDraw(canvas!!)
    }

    /**
     * 绘制
     */
    private fun customOnDraw(canvas: Canvas) {
        if (isDefaultPic) {
            //如果drawable不为null直接绘制drawable否则绘制默认背景图
            if (mBackgroundDrawable != null) {
                mBackgroundDrawable?.draw(canvas)
                return
            }
            drawDefaultPic(canvas)
        }
    }

    /**
     * 绘制默认的图片样式带纯背景色加文字 animationControl整体使用animationControl控制属性动画
     */
    private fun drawDefaultPic(canvas: Canvas) {
        mPaint.alpha = 255
        val saveLayerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), mPaint)
        //过渡渐变
        //dst
        mPaint.color = backgroundFontColorInt
        paintAlpha()
        if (isOval) {
            canvas.drawCircle(width / 2F, height / 2F, width / 2F, mPaint)
        } else {
            canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), mPaint)
        }
        //绘制字体
        onDrawFont(canvas)

        //设置图层混合模式
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        //src
        mPaint.color = ContextCompat.getColor(AppUtil.application, R.color.white)
        if (isOval) {
            canvas.drawCircle(width / 2F, height / 2F, (width / 2) * (1 - animationControl), mPaint)
        } else {
            canvas.drawRect(
                (width / 2F) * animationControl,
                (height / 2F) * animationControl,
                (width / 2F) + ((1 - animationControl) * (width / 2F)),
                (height / 2F) + ((1 - animationControl) * (height / 2F)),
                mPaint
            )
        }
        //图层混合取消
        mPaint.xfermode = null

        canvas.restoreToCount(saveLayerId)
    }

    /**
     * 绘制字
     */
    private fun onDrawFont(canvas: Canvas) {
        val rect = Rect(0, 0, width, height)

        //paint 设置
        mPaint.color = fontColor
        paintAlpha()
        mPaint.textSize = fontSize
        //文字对其方式设置为居中对齐
        mPaint.textAlign = Paint.Align.CENTER

        //计算文字baseline
        val baseLine = mPaint.fontMetrics.let {
            val distance = (it.descent - it.ascent) / 2 - it.bottom
            rect.centerY() + distance
        }

        //绘制
        canvas.drawText(displayText, rect.centerX().toFloat(), baseLine, mPaint)
    }

    /**
     * alpha 设置
     */
    private fun paintAlpha() {
        mPaint.alpha = animationControl.let {
            if (it < 0.3F) {
                100
            } else {
                255
            }
        }
    }
}