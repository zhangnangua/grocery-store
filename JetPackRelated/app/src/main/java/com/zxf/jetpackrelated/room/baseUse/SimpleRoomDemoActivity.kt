package com.zxf.jetpackrelated.room.baseUse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.zxf.jetpackrelated.databinding.ActivitySimpleUseRoomBinding
import kotlinx.coroutines.launch

/**
 * 作者： zxf
 * 描述： 简单增删改查测试页面
 */
class SimpleRoomDemoActivity : AppCompatActivity() {

    /**
     * binding 自定义简单懒加载，可以在destroy中释放
     */
    private var _binding: ActivitySimpleUseRoomBinding? = null
    private val binding get() = _binding!!

    /**
     * 数据库dao
     */
    private val simpleDao: SimpleStudentDao by lazy(LazyThreadSafetyMode.NONE) {
        SimpleMyDataBase.getDataBase().simpleStudentDao()
    }

    /**
     * viewModel
     */
    lateinit var viewModel: SimpleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySimpleUseRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initParam()
        initView()
    }

    private fun initParam() {
        viewModel = ViewModelProvider(
            this,
            //传入自己的工厂
            MyViewModelFactory(simpleDao)
        )[SimpleViewModel::class.java]
    }

    private fun initView() {
        with(binding) {
            btnInsert.setOnClickListener {
                viewModel.insertStudent(SimpleStudentEntity(0, "zxf", "18"))
            }
            btnInsertAll.setOnClickListener {
                viewModel.insertStudentAll(
                    arrayListOf(
                        SimpleStudentEntity(0, "liSi", "18"),
                        SimpleStudentEntity(0, "wangWu", "18")
                    )
                )
            }
            //delete 和 update 是根据什么来的  看了源码生成的sql默认根据主键来的
            btnDelete.setOnClickListener {
                viewModel.deleteStudent(SimpleStudentEntity(2, "delete", "99"))
//                viewModel.deleteStudent(SimpleStudentEntity(199,"update","99"))
            }
            btnUpdate.setOnClickListener {
                //所以我们这里面可以直接写一个默认的id去设置,不需要非要拿到查询的对象，(先查询在更新删除？？？？)
//                viewModel.updateStudent(SimpleStudentEntity(1,"update","99"))
                viewModel.updateStudent(SimpleStudentEntity(199, "update", "99"))
            }
            //看了一下查询生成的源代码，对于对象来说直接new了，所以不会返回null，但是对象的值，没有就是null，需要声明为可null类型
            btnGetId.setOnClickListener {
                lifecycleScope.launch {
                    displayToTextView(viewModel.getStudentById(5))
                }
            }
            btnGetAll.setOnClickListener {
                lifecycleScope.launch {
                    displayToTextView(viewModel.getStudentAll())
                }
            }
            btnTransactionInsertGet.setOnClickListener {
                lifecycleScope.launch {
                    displayToTextView(
                        viewModel.transactionInsertGet(
                            SimpleStudentEntity(
                                name = "transaction",
                                age = "99"
                            )
                        )
                    )
                }
            }
        }
    }

    private fun displayToTextView(students: List<SimpleStudentEntity>) {
        val string = students.joinToString(
            """
            
        """.trimIndent()
        )
        binding.text.text = string
    }

    /**
     * 释放binding
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}