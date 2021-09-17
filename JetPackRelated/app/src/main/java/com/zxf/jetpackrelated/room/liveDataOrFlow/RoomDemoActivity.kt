package com.zxf.jetpackrelated.room.liveDataOrFlow

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.zxf.jetpackrelated.databinding.ActivitySimpleUseRoomBinding
import com.zxf.jetpackrelated.room.liveDataOrFlow.flow.StudentFactory
import com.zxf.jetpackrelated.room.liveDataOrFlow.flow.StudentViewModel
import com.zxf.jetpackrelated.room.liveDataOrFlow.liveData.StudentLiveDataViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

        //lifecycleScope 默认调度器主线程 主从作用域 根据lifecycleStore的生命周期判断协程的取消 直接拉取显示
        lifecycleScope.launch {
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

    fun setEvent() {
        with(binding) {
            btnInsert.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.insertStudent(StudentEntity(0, "zxf", "18"))
                }
            }
            //delete 和 update 是根据什么来的  看了源码生成的sql默认根据主键来的
            btnDelete.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.deleteStudent(StudentEntity(2, "delete", "99"))
                }
            }
            btnUpdate.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.updateStudent(StudentEntity(199, "update", "99"))
                }
            }
            btnInsertAll.visibility = View.GONE
            btnGetId.visibility = View.GONE
            btnGetAll.visibility = View.GONE
            btnTransactionInsertGet.visibility = View.GONE
        }
    }

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