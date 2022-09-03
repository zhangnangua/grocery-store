package com.zxf.jetpackrelated.room.liveDataOrFlow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.withTransaction
import com.zxf.jetpackrelated.databinding.ActivitySimpleUseRoomBinding
import com.zxf.jetpackrelated.room.liveDataOrFlow.flow.StudentFactory
import com.zxf.jetpackrelated.room.liveDataOrFlow.flow.StudentViewModel
import com.zxf.jetpackrelated.room.liveDataOrFlow.migration.FruitEntity
import com.zxf.utils.DbUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 作者： zxf
 * 描述： roomDemoActivity
 */
class RoomDemoActivity : AppCompatActivity() {

    /**
     * binding
     */
    private var _binding: ActivitySimpleUseRoomBinding? = null
    private val binding: ActivitySimpleUseRoomBinding
        get() = _binding!!

    /**
     * viewModel
     */
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        //flow viewModel
        val dao = StudentDataBase.getDataBase().getStudentFlowDao()
        ViewModelProvider(this, StudentFactory(dao))[StudentViewModel::class.java]
        //live viewModel
//        ViewModelProvider(this)[StudentLiveDataViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySimpleUseRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
        }

        //lifecycleScope 默认调度器主线程 主从作用域 根据lifecycleStore的生命周期判断协程的取消 直接拉取显示
        lifecycleScope.launch(exceptionHandler) {
            //flow
            viewModel.obtainStudentAllUseFlow()
                .collect {
                    displayToTextView(it)
                }
        }

        //liveData
//        viewModel.obtainStudentAllUseLiveData()
//            .observe(this@RoomDemoActivity, object : Observer<List<StudentEntity>> {
//                override fun onChanged(t: List<StudentEntity>?) {
//                    displayToTextView(t!!)
//                }
//            })

        setEvent()
    }

    @SuppressLint("SetTextI18n")
    fun setEvent() {
        with(binding) {
            btnInsert.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.insertStudent(StudentEntity(0, "zxf", "18","addColumn"))
                }
            }
            //delete 和 update 是根据什么来的  看了源码生成的sql默认根据主键来的
            btnDelete.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.deleteStudent(StudentEntity(2, "delete", "99","addColumn"))
                }
            }
            btnUpdate.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.updateStudent(StudentEntity(199, "update", "99","addColumn"))
                }
            }
            btnInsertAll.visibility = View.GONE
            btnGetId.visibility = View.GONE
            btnGetAll.visibility = View.GONE
            btnTransactionInsertGet.visibility = View.GONE

            /**
             * 升级测试
             */
            btnTransactionInsertGet.visibility = View.VISIBLE
            btnTransactionInsertGet.text = "MIGRATION TEST"
            btnTransactionInsertGet.setOnClickListener {
                val conflateEntityDao = StudentDataBase.getDataBase().getConflateEntityDao()
                lifecycleScope.launch {
                    StudentDataBase.getDataBase().withTransaction {
                        conflateEntityDao.insertFruit(
                            FruitEntity(
                                text = "apple",
                                text2 = "other apple2"
                            )
                        )
                        val obtainFruit = conflateEntityDao.obtainFruit()
                        withContext(Dispatchers.Main.immediate) {
                            binding.text.text = obtainFruit.toString()
                        }
                    }
                }
            }

            /**
             * key-value Test
             */
            btnGetId.visibility = View.VISIBLE
            btnGetId.text = "INSERT KEY VALUE"
            btnGetId.setOnClickListener {
                lifecycleScope.launch() {
                    DbUtil.withTransaction {
                        DbUtil.writeCache("zxf", "hahahhahah")
                        DbUtil.writeCache("zxf", "recover test")
                        DbUtil.writeCache("ddd", "hhhhhhh")
                        DbUtil.writeCache("ddd", "recover test")
                    }
                }
            }
            btnGetAll.visibility = View.VISIBLE
            btnGetAll.text = "OBTAIN KEY VALUE"
            btnGetAll.setOnClickListener {
                lifecycleScope.launch {
                    withContext(Dispatchers.IO){
                        val value = DbUtil.obtainValue("zxf")
                        withContext(Dispatchers.Main){
                            binding.text.text = value
                        }
                    }
                }
            }
        }
    }

    /**
     * student
     */
    private fun displayToTextView(students: List<StudentEntity>) {
        val string = students.joinToString(
            """
            
        """.trimIndent()
        )
        binding.text.text = string
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}