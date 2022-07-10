package com.howie.multiple_process.aidl_service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import com.howie.multiple_process.BuildConfig
import com.howie.multiple_process.IBookManagerService
import com.howie.multiple_process.bean.Book
import com.howie.multiple_process.helper.AppUtil
import com.howie.multiple_process.listener.IBookSizeChangeListener
import java.lang.Boolean
import kotlin.synchronized

class BookManagerService : Service() {
    override fun onBind(intent: Intent?): IBinder = BookManagerServiceImpl
}

/**
 * 函数在binder线程池中执行，注意并发问题
 */
object BookManagerServiceImpl : IBookManagerService.Stub() {

    private const val TAG = "BookManagerServiceImpl"

    /**
     * 专门用于AIDL服务端保存回调接口,内部使用binder作为key。保证客户端传入同一个对象，在服务端可以顺利的找到该对象。
     */
    private val callbacks: RemoteCallbackList<IBookSizeChangeListener> = RemoteCallbackList()

    private val books = ArrayList<Book>()

    override fun getBookList(): MutableList<Book> {
        //mock time consuming
        Thread.sleep(3000)
        return books
    }

    override fun addBook(book: Book?) {
        if (AppUtil.isDebug) {
            Log.d(TAG, "addBook: book is ${book?.toString()}")
        }

        //do 测试out服务端的改动，可以改变客户端的对象
        book?.let {
            it.bookDescribe = "it modify by service ."
        }

        //在binder线程池进行操作，故需要进行同步处理
        synchronized(books) {
            books.add(book ?: Book())
        }

        //回调callback
        synchronized(callbacks) {
            try {
                val num = callbacks.beginBroadcast()
                if (AppUtil.isDebug) {
                    Log.d(TAG, "callback num is $num")
                }
                for (i in 0 until num) {
                    callbacks.getBroadcastItem(i)?.bookSizeChange(books)
                }
            } finally {
                callbacks.finishBroadcast()
            }
        }

    }

    override fun registerListener(listener: IBookSizeChangeListener?) {
        if (AppUtil.isDebug) {
            Log.d(
                TAG,
                "Service IBookSizeChangeListener hash code is ${listener?.hashCode()}"
            )
            Log.d(
                TAG,
                "Service IBookSizeChangeListener binder is ${listener?.asBinder()}"
            )
        }
        //RemoteCallbackList  内部保证了线程安全，故不需要加锁
        listener?.asBinder()?.linkToDeath(object : IBinder.DeathRecipient {
            override fun binderDied() {
                // TODO: 收到死亡通知，可以做一些事情

            }

        }, 0)
        if (listener != null) {
            callbacks.register(listener)
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
                TAG,
                "Service IBookSizeChangeListener hash code is ${listener?.hashCode()}"
            )
            Log.d(
                TAG,
                "Service IBookSizeChangeListener binder is ${listener?.asBinder()}"
            )
        }
        //RemoteCallbackList  内部保证了线程安全，故不需要加锁
        if (listener != null) {
            callbacks.unregister(listener)
        }
    }

}