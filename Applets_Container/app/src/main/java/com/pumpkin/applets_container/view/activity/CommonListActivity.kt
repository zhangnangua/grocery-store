package com.pumpkin.applets_container.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.data.repo.AbsCommonListRepo
import com.pumpkin.applets_container.databinding.ActivityCommonListBinding
import com.pumpkin.applets_container.viewmodel.CommonListViewModel
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.BaseAdapter
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.mvvm.viewmodel.PACViewModelProviders
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CommonListActivity : BaseActivity() {
    private var title: String? = null
    lateinit var binding: ActivityCommonListBinding
    lateinit var adapter: BaseAdapter

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        PACViewModelProviders.of(this)[CommonListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!parseParameter(savedInstanceState) || title == null) {
            if (AppUtil.isDebug) {
                Log.d(TAG, "onCreate () -> finish . title $title .")
            }
            finish()
            return
        }
        initView()
        loadData()
    }

    private fun parseParameter(savedInstanceState: Bundle?): Boolean {
        val bundle: Bundle? = savedInstanceState ?: intent.extras
        if (bundle != null) {
            title = bundle.getString(CommonListParameterBuilder.KEY_TITLE)
            val repoC = (bundle.getSerializable(CommonListParameterBuilder.KEY_REPO) as? Class<out AbsCommonListRepo>)
                ?: return false
            return try {
                viewModel.attach(repoC.newInstance())
                true
            } catch (e: Exception) {
                if (AppUtil.isDebug) {
                    throw e
                }
                false
            }
        }
        return false
    }

    private fun initView() {
        setTitle(title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityCommonListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = BaseAdapter(Glide.with(this), this)
        binding.rv.let {
            it.layoutManager = LinearLayoutManager(this@CommonListActivity)
            it.adapter = adapter
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            // 每次生命周期处于 STARTED 状态（或更高状态）时，repeatOnLifecycle 在新的协程中启动块，并在它停止时取消它。
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flow().onEach {
                    adapter.setData(it)
                }.collect()
            }
            //如果这里被执行，则代表生命周期已经走到了onDestroy，因为repeatOnLifecycle是挂起函数，在生命周期为onDestroy的时候进行了恢复。
        }
        viewModel.request()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG = "CommonListActivity"

        fun go(context: Context, builder: CommonListParameterBuilder) {
            context.startActivity(Intent(context, CommonListActivity::class.java).apply {
                putExtras(builder.getBundle())
            })
        }
    }

}

class CommonListParameterBuilder {
    private val bundle = Bundle()

    fun setTitle(title: String): CommonListParameterBuilder {
        bundle.putString(KEY_TITLE, title)
        return this
    }

    fun <T : AbsCommonListRepo> setRepo(viewModelC: Class<T>): CommonListParameterBuilder {
        bundle.putSerializable(KEY_REPO, viewModelC)
        return this
    }

    fun getBundle() = bundle

    companion object {
        const val KEY_TITLE = "k.title"
        const val KEY_REPO = "k.repo"
    }

}