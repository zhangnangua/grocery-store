package com.howie.multiple_process.aidl_service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.howie.multiple_process.IBookManagerService
import com.howie.multiple_process.bean.Book
import com.howie.multiple_process.helper.AppUtil
import com.howie.multiple_process.listener.IBookSizeChangeListener

class BookManagerService : Service() {
    override fun onBind(intent: Intent?): IBinder = BookManagerServiceImpl
}

/**
 * 函数在binder线程池中执行，注意并发问题
 */
object BookManagerServiceImpl : IBookManagerService.Stub() {

    private val books = ArrayList<Book>()

    override fun getBookList(): MutableList<Book> {
        return synchronized(this) {
            //mock time consuming
            Thread.sleep(3000)
            books
        }
    }

    override fun addBook(book: Book?) {
        synchronized(this) {
            books.add(book ?: Book())
        }
    }

    override fun registerListener(listener: IBookSizeChangeListener?) {
        if (AppUtil.isDebug) {
            Log.d(
                "BookManagerServiceImpl",
                "Service IBookSizeChangeListener hash code is ${listener?.hashCode()}"
            )
            Log.d(
                "BookManagerServiceImpl",
                "Service IBookSizeChangeListener binder is ${listener?.asBinder()}"
            )
        }
    }

    /**
     * 注意，这里接收到的listener对象和客户端传递的不是同一个对象，因为在不同的进程。通过Binder传递到服务端后，却会产生两个全新的对象。
     * 对象的跨进程传输本质上都是反序列化的过程。
     *
     * 所以如何判断传递过来的对象是我们需要解除注册的对象呢？
     * 相同对象的binder对应服务端binder只有一个，所以可以用binder来进行校验
     */
    override fun unRegisterListener(listener: IBookSizeChangeListener?) {
        if (AppUtil.isDebug) {
            Log.d(
                "BookManagerServiceImpl",
                "Service IBookSizeChangeListener hash code is ${listener?.hashCode()}"
            )
            Log.d(
                "BookManagerServiceImpl",
                "Service IBookSizeChangeListener binder is ${listener?.asBinder()}"
            )
        }
    }

}