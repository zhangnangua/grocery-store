package com.zxf.learn_test.mvp_decoupling.mvp2.map_base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zxf.jetpackrelated.utils.ToastUtil
import java.lang.ref.SoftReference

interface Model
interface View

/**
 * in 逆变 , out协变
 */
interface Presenter<V : View, M : Model> {

    /**
     * 注册view
     */
    fun registerView(view: V)

    /**
     * 注册model
     */
    fun registerModel(model: M)

    /**
     * 销毁
     */
    fun destroy()

}

/**
 * 基础的presenter类
 */
abstract class BasePresenter<V : View, M : Model> : Presenter<V, M> {

    /**
     * view使用弱引用保证不内存泄漏
     */
    @Volatile
    private var srv: SoftReference<V>? = null

    /**
     * 获取view
     */
    val view: V?
        get() {
            val snapSrv = srv
            return if (snapSrv == null) {
                throw IllegalAccessException("view未注册或者已经被销毁")
            } else {
                snapSrv.get()
            }
        }

    /**
     * model
     */
    protected var model: M? = null

    override fun registerModel(model: M) {
        this.model = model
    }

    override fun registerView(view: V) {
        this.srv = SoftReference(view)
    }

    override fun destroy() {
        val snapSrv = srv
        if (snapSrv != null) {
            snapSrv.clear()
            srv = null
        }
        model = null
        onDestroy()
    }

    abstract fun onDestroy()

}

/**
 * BaseMVP
 */
interface BaseMVP<V : View, M : Model, P : BasePresenter<V, M>> {
    fun createPresenter(): P
    fun createView(): V
    fun createModel(): M
}

/**
 * 基础的Activity
 */
abstract class BaseMvpActivity<V : View, M : Model, P : BasePresenter<V, M>> :
    AppCompatActivity(),
    BaseMVP<V, M, P> {

    protected var presenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val p = createPresenter()
        p.registerModel(createModel())
        p.registerView(createView())
        this.presenter = p
    }

    fun showToast(info: String) {
        ToastUtil.toastShort(info)
    }

    fun showLongToast(info: String) {
        ToastUtil.toastLong(info)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.destroy()
    }

}
























































