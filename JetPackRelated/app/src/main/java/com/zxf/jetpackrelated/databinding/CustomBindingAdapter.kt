package com.zxf.jetpackrelated.databinding

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.zxf.jetpackrelated.utils.LogUtil
import com.zxf.jetpackrelated.utils.isNotNullOrEmpty

/**
 * 作者： zxf
 * 描述： 自定义bindingAdapter
 *
 * BindingAdapter  必须绑定静态方法
 * 两种办法
 * 第一种在半生对象里面增加JvmStatic注解
 * 第二种 直接声明顶级函数，这种函数编译成java默认就是静态函数  这里直接使用第二种办法
 *
 *
 * BindingAdapter 在何时被调用？
 * 查看源码发现，在inflate视图的时候，此时会创建对应布局的Impl对象，
 * 在其构造函数内调用了父类的构造函数以及自身的invalidateAll 最终会调用到executeBindings里面会调用这里声明的方法，此时就进行参数绑定了
 */

/**
 * 加载网络图片
 */
//@BindingAdapter("image")
//fun setImage(imageView: ImageView, imageUrl: String?) {
//    if (imageUrl.isNotNullOrEmpty()) {
//        Glide.with(imageView)
//            .load(imageUrl)
//            .into(imageView)
//    } else {
//        imageView.setBackgroundColor(Color.BLUE)
//    }
//}

/**
 * 方法重载
 * 加载本地资源图片
 */
//@BindingAdapter("image")
//fun setImage(imageView: ImageView, @DrawableRes imageRes: Int) {
//    imageView.setImageResource(imageRes)
//}

/**
 * 上面的两种重载方式，直接可以写成多重重载
 * 如下所示:
 * 方法的参数以value={""，""}的形式存在
 * 变量requireAll用于告诉DataBinding库这些参数是否都要赋值，默认值为true，即全部需要赋值,这里写成false;
 * 如果设置为true，则对应的属性必须在xml一起生命，否则编译报错
 */
@BindingAdapter(value = ["image", "defaultImageRes"], requireAll = false)
fun setImage2(imageView: ImageView, imageUrl: String?, @DrawableRes imageRes: Int) {
    if (imageUrl.isNotNullOrEmpty()) {
        Glide.with(imageView)
            .load(imageUrl)
            .into(imageView)
    } else {
        imageView.setImageResource(imageRes)
    }
}

/**
 * 可选旧值
 * 在某些情况下，你可能希望在方法中得到该属性的旧值。例如，在修改控件的padding时，我们可能希望得到修改前的padding，以防止方法重复调用。代码如下所示。
 * 需要注意的是，使用可选旧值时，方法中的参数顺序需要先写旧值，后写新值。即oldPadding在前，newPadding在后。
 */
@BindingAdapter(value = ["padding"])
fun setPadding(view: View, oldValue: Int, newValue: Int) {
    LogUtil.d("------------paddingValueLog: oldValue:$oldValue newValue:$newValue")
    if (oldValue != newValue) {
        view.setPadding(newValue, newValue, newValue, newValue)
    }
}


/**
 * JvmStatic 修饰的静态方法实现
 */

//class ImageViewAdapter{
//    companion object{
//        @BindingAdapter("image")
//        @JvmStatic
//        fun setImage(imageView: ImageView, imageUrl: String?) {
//            if (imageUrl.isNotNullOrEmpty()) {
//                Glide.with(imageView)
//                    .load(imageUrl)
//                    .into(imageView)
//            } else {
//                imageView.setBackgroundColor(Color.BLUE)
//            }
//        }
//    }
//}

//object ImageViewAdapter{
//    @BindingAdapter("image")
//    fun setImage(imageView: ImageView, imageUrl: String?) {
//        if (imageUrl.isNotNullOrEmpty()) {
//            Glide.with(imageView)
//                .load(imageUrl)
//                .into(imageView)
//        } else {
//            imageView.setBackgroundColor(Color.BLUE)
//        }
//    }
//}

