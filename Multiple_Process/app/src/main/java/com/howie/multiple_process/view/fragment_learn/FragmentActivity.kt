package com.howie.multiple_process.view.fragment_learn

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.howie.multiple_process.R
import com.howie.multiple_process.databinding.ActivityFragmentBinding

/**
 * 学习fragment 源码类
 */
class FragmentActivity : AppCompatActivity() {

    lateinit var binding: ActivityFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addFragment()
    }

    private fun addFragment() {
        val tag = "howie"
        val fragmentManager = supportFragmentManager
        val ft = fragmentManager.beginTransaction()
        val fragment = fragmentManager.findFragmentByTag(tag)
        if (fragment != null) {
            ft.show(fragment)
        } else {
            ft.add(R.id.fl_content, LearnFragment(), tag)
        }
//        ft.hide()
//        ft.setMaxLifecycle()
//        ft.detach()
//        ft.addToBackStack()
        ft.commitAllowingStateLoss()
    }


}