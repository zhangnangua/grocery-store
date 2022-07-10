package com.howie.multiple_process.view.aidlClient

import ToastUtil
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.howie.multiple_process.IBookManagerService
import com.howie.multiple_process.IRemoteService
import com.howie.multiple_process.aidl_service.BookManagerService
import com.howie.multiple_process.aidl_service.RemoteService
import com.howie.multiple_process.bean.Book
import com.howie.multiple_process.databinding.ActivityClientBinding
import com.howie.multiple_process.helper.AppUtil
import com.howie.multiple_process.helper.safeLaunch
import com.howie.multiple_process.listener.IBookSizeChangeListener
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class AbstractClientActivity : AppCompatActivity() {

    lateinit var binding: ActivityClientBinding

    /**
     * simple aidl example
     */
    private var iRemoteService: IRemoteService? = null

    /**
     * register/unregister book callback
     */
    private val callback = object : IBookSizeChangeListener.Stub() {
        override fun bookSizeChange(books: MutableList<Book>?) {
            //binder 线程池中执行 需要调度到主线程
            Log.d(TAG, "callback book size is ${books?.size}")
            safeLaunch {
                binding.tvDisplayBook.text = books?.joinToString("\t\n")
            }
        }
    }

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iRemoteService = IRemoteService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            unBind()
        }

        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
            unBind()
        }
    }

    /**
     * bookManager aidl example
     */
    private var iBookManagerService: IBookManagerService? = null
    private val mBookManagerConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iBookManagerService = IBookManagerService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            unBindBookManagerService()
        }

        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
            unBindBookManagerService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btDescribe.text = this::class.java.simpleName

        // bind 远程aidl服务
        bindRemoteService()
        // bind 远程aidl BookManagerService
        bindBookManagerService()

        event()
    }

    override fun onDestroy() {
        super.onDestroy()
        iBookManagerService?.unRegisterListener(callback)
        unBind()
        unBindBookManagerService()
    }

    private fun bindRemoteService() {
        bindService(
            Intent(this, RemoteService::class.java),
            mConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun bindBookManagerService() {
        bindService(
            Intent(this, BookManagerService::class.java),
            mBookManagerConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun unBind() {
        iRemoteService = null
        unbindService(mConnection)
    }

    private fun unBindBookManagerService() {
        iBookManagerService = null
        unbindService(mBookManagerConnection)
    }

    private fun event() {
        // 获取远程进程PID
        binding.btObtainRemotePid.setOnClickListener {
            safeLaunch(context = Dispatchers.IO, start = CoroutineStart.DEFAULT) {
                ToastUtil.toastShort("Remote pid is ${iRemoteService?.pid ?: ""}.")
            }
        }
        // add book to BookManager
        binding.btAddBook.setOnClickListener {
            var bookId = 0
            safeLaunch(context = Dispatchers.IO) {
                iBookManagerService?.addBook(
                    Book(
                        ++bookId,
                        "Hello,Howie$bookId",
                        "No description here."
                    )
                )
            }
        }
        //obtain book list
        binding.btObtainRemoteBook.setOnClickListener {
            ToastUtil.toastShort("loading...")
            safeLaunch(context = Dispatchers.IO) {
                val books = iBookManagerService?.bookList?.joinToString("\t\n")
                if (!TextUtils.isEmpty(books)) {
                    withContext(Dispatchers.Main) {
                        ToastUtil.toastShort("loading success.")
                        binding.tvDisplayBook.text = books
                    }
                }
            }
        }
        binding.btRegisterBookCallback.setOnClickListener {
            if (AppUtil.isDebug) {
                Log.d(
                    TAG,
                    "Client IBookSizeChangeListener hash code is ${callback.hashCode()}"
                )
                Log.d(
                    TAG,
                    "Client IBookSizeChangeListener binder is ${callback.asBinder()}"
                )
            }
            iBookManagerService?.registerListener(callback)
        }
        binding.btUnregisterBookCallback.setOnClickListener {
            if (AppUtil.isDebug) {
                Log.d(
                    TAG,
                    "Client IBookSizeChangeListener hash code is ${callback.hashCode()}"
                )
                Log.d(
                    TAG,
                    "Client IBookSizeChangeListener binder is ${callback.asBinder()}"
                )
            }
            iBookManagerService?.unRegisterListener(callback)
        }

        // TODO: 多进程文件写入测试
        //全局异常捕获
//        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
//            throwable.printStackTrace()
//        }
//        binding.btDescribe.setOnClickListener {
//            // 测试多进程同一文件写入，client1 写入
//            //一般使用lifecycleScope，这里为了测试使用GlobalScope
//            GlobalScope.launch(
//                context = Dispatchers.Main.immediate + coroutineExceptionHandler,
//                start = CoroutineStart.DEFAULT
//            ) {
//                writeString(content = "i am client2")
//            }
//        }
    }


    companion object {
        private const val TAG = "BookManagerServiceImpl"
    }
}