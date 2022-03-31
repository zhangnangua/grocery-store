package com.zxf.jetpackrelated.room.baseUse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import kotlinx.coroutines.*

/**
 * 作者： zxf
 * 描述： viewModel
 */
class SimpleViewModel(private val simpleStudentDao: SimpleStudentDao) : ViewModel() {

    fun insertStudent(studentEntity: SimpleStudentEntity) {
        viewModelScope.launch(Dispatchers.Default) {
            simpleStudentDao.insertStudent(studentEntity)
        }
    }

    fun insertStudentAll(studentEntity: List<SimpleStudentEntity>) {
        viewModelScope.launch(Dispatchers.Default) {
            simpleStudentDao.insertStudentAll(studentEntity)
        }
    }

    fun deleteStudent(studentEntity: SimpleStudentEntity) {
        viewModelScope.launch(Dispatchers.Default) {
            simpleStudentDao.deleteStudent(studentEntity)
        }
    }

    fun updateStudent(studentEntity: SimpleStudentEntity) {
        viewModelScope.launch(Dispatchers.Default) {
            simpleStudentDao.updateStudent(studentEntity)
        }
    }

    suspend fun getStudentAll(): List<SimpleStudentEntity> {
        //使用主从作用域
        return supervisorScope {
            val students = async(Dispatchers.IO) {
                simpleStudentDao.getStudentAll()
            }
            students.await()
        }
    }

    suspend fun getStudentById(id: Int): List<SimpleStudentEntity> {
        //使用主从作用域
        return supervisorScope {
            val students = async(Dispatchers.IO) {
                simpleStudentDao.getStudentById(id)
            }
            students.await()
        }
    }

    suspend fun suspendUseTest(name: String) {
        return withContext(Dispatchers.IO) {
            simpleStudentDao.suspendStudentByName(name)
        }
    }

    /**
     * 依赖suspend dao 错误使用事务的使用示例
     * 看似是安全的，但实际上这段代码存在着安全问题:
     *
     * Android 的 SQLite 事务是受制于单个线程的。当一个正在进行的事务中的某个查询在当前线程中被执行时，它会被视为是该事务的一部分并允许继续执行。但当这个查询在另外一个线程中被执行时，
     * 那它就不再属于这个事务的一部分了，这样的话就会导致这个查询被阻塞，直到事务在另外一个线程执行完成。
     *
     * 这也是 beginTransaction 和 endTransaction 这两个 API 能够保证原子性的一个前提。当数据库的事务操作都是在一个线程上完成的，这样的 API 不会有任何问题，但是使用协程之后问题就来了，因为协程是不绑定在任何特定的线程上的。
     * 也就是说，问题的根源就是在协程挂起之后会继续执行所绑定的那个线程，而这样是不能保证和挂起之前所绑定的线程是同一个线程。
     */
    suspend fun transactionUseError(studentEntity: SimpleStudentEntity): List<SimpleStudentEntity> {
        //使用了 IO dispatcher，所以该 DB 的操作在 IO 线程上进行
        return withContext(Dispatchers.IO) {
            val dataBase = SimpleMyDataBase.getDataBase()
            //在 IO-Thread-1 线程上开始执行事务
            dataBase.beginTransaction()
            return@withContext try {
                // 协程可以在与调度器（这里就是 Dispatchers.IO）相关联的任何线程上绑定并继续执行。同时，由于事务也是在 IO-Thread-1 中开始的，因此我们可能恰好可以成功执行查询。
                simpleStudentDao.suspendInsertStudent(studentEntity)//挂起函数
                // 如果协程又继续在 IO-Thread-2 上执行，那么下列操作数据库的代码可能会引起死锁，因为它需要等到 IO-Thread-1 的线程执行结束后才可以继续。
                simpleStudentDao.suspendStudentByName(studentEntity.name!!).apply {
                    dataBase.setTransactionSuccessful() //永远不会执行这一行
                }
            } finally {
                dataBase.endTransaction()//永远不会执行这一行
                SimpleStudentEntity(name = null, age = null)
            }
        }
    }

    /**
     * 简单解决上述问题，固定一个线程。参考RoomDatabase{@link #runInTransaction(Runnable)}方法 就是这么干的
     *
     * 但是当在挂起代码块中使用另一个调度器的话就会出问题了: 不能在里面继续调度 withContext 以及 launch调度到正常的线程池了
     *
     * withTransaction API 在上下文中创建了三个关键元素:
     * 单线程调度器，用于执行数据库操作；上下文元素，帮助 DAO 函数判断其是否处在事务中；ThreadContextElement，用来标记事务协程中所使用的调度线程。
     */
    suspend fun simpleSolveTransactionUseError(studentEntity: SimpleStudentEntity): List<SimpleStudentEntity> {
//        return withContext(newSingleThreadContext("db")) {
//            val dataBase = SimpleMyDataBase.getDataBase()
//            dataBase.beginTransaction()
//            try {
//                simpleStudentDao.suspendInsertStudent(studentEntity)
//                return@withContext simpleStudentDao.suspendStudentByName(studentEntity.name!!)
//                    .apply {
//                        dataBase.setTransactionSuccessful()
//                    }
//            } finally {
//                dataBase.endTransaction()
//            }
//        }
        return SimpleMyDataBase.getDataBase().simpleSolveTransactionUseError {
//            simpleStudentDao.suspendInsertStudent(studentEntity)
//            simpleStudentDao.suspendStudentByName(studentEntity.name!!)
            withContext(Dispatchers.Default){
            simpleStudentDao.suspendInsertStudent(studentEntity)
            simpleStudentDao.suspendStudentByName(studentEntity.name!!)
            }
        }
    }

    suspend fun <T> RoomDatabase.simpleSolveTransactionUseError(block: suspend () -> T): T {
        return withContext(newSingleThreadContext("sqlite")) {
            beginTransaction()
            try {
                return@withContext block()
            } finally {
                endTransaction()
            }
        }
    }

    /**
     * 最终解决方案 RoomDatabase扩展方法 withTransaction api，模仿了 withContext API，但是提供了专为安全执行 Room 事务而构建的协程上下文.
     *
     * 依赖suspend dao 正确使用事务的使用示例
     * 先插入在获取，dao声明为suspend的方式，使用事务,
     * 借用room的扩展函数withTransaction(内部保证了，第一个挂起函数执行完毕之后，和第二个在同一个线程) 保证事务的一致性，
     */
    suspend fun transactionInsertGet(studentEntity: SimpleStudentEntity): List<SimpleStudentEntity> {
        return SimpleMyDataBase.getDataBase().withTransaction {
            withContext(Dispatchers.Default) {
                simpleStudentDao.suspendInsertStudent(studentEntity)
                simpleStudentDao.suspendStudentByName(studentEntity.name!!)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }
}

/**
 * 自定义工厂，可传入Dao参数
 */
class MyViewModelFactory(private val dao: SimpleStudentDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SimpleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SimpleViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class.")
    }

}