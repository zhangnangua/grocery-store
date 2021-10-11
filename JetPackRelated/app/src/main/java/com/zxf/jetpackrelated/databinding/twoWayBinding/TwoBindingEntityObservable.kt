package com.zxf.jetpackrelated.databinding.twoWayBinding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.zxf.jetpackrelated.utils.isNotNullOrEmpty

/**
 * 作者： zxf
 * 描述： 双向绑定可观察者实体类
 */
class DisplayEntity(
    var displayStr: String,
    val disturbTest: String = "disturb"
)

//region 双向绑定实现一  直接继承BaseObservable
class TwoBindingEntityObservable : BaseObservable() {
    private val displayEntity: DisplayEntity = DisplayEntity("双向绑定测试")

    /**
     * 在getter 方法上面增加@Bindable  告诉编译器希望对这个字段进行双向绑定
     * 在xml中直接使用displayStr 进行绑定
     */
    @Bindable
    fun getDisplayStr(): String = displayEntity.displayStr

    /**
     * setter 方法在用户编译edittext的时候被自动调用,需要在这里面对displayStr字段进行手动更新
     * 调用notifyPropertyChanged方法通知观察者数据已变更
     * 需要对值进行判断否则会产生循环调用的问题
     */
    fun setDisplayStr(displayStr: String) {
        //需要对值进行判断否则会产生循环调用的问题
        if (displayStr.isNotNullOrEmpty() && displayStr == displayEntity.displayStr) {
            return
        }
        displayEntity.displayStr = displayStr
        notifyPropertyChanged(BR.displayStr)
    }
}
//endregion

//region  算共享绑定实现二  使用Observable系列  这里使用ObservableField
class TwoBindingEntityObservable2 {

    /**
     * 源码里面是按照属性来的  比如上面的disturb属性就没有在BindingImp里面进行实现
     * 之刷新在xml文件里面使用双向绑定的数据 对页面进行刷新
     *
     * 这种写法无法做到视图数据改变后，通知数据然后在通知视图。因为在实现的BindingImpl里面 调用的是displayEntityField.get().setDisplayStr
     * 相当于没有直接调用displayEntityField.set 所以刷新不到界面
     */
    val displayEntityField = ObservableField(DisplayEntity("双向绑定测试-use observableField"))
}
//endregion

//region  算共享绑定实现二  使用Observable系列  这里使用ObservableField  用于测试直接封装String  和 封装对象的区别

/**
 * 下面的写法可以实现数据和视图的双向绑定
 * 因为在实现的BindingImpl里面 调用的是displayEntityField.set
 */
class TwoBindingEntityObservable3 {

    /**
     * 源码里面是按照属性来的  比如上面的disturb属性就没有在BindingImp里面进行实现
     * 之刷新在xml文件里面使用双向绑定的数据 对页面进行刷新
     */
    val displayEntityField = ObservableField("双向绑定测试-use observableField")
}
//endregion
