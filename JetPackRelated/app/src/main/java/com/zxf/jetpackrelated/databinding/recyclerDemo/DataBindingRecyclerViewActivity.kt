package com.zxf.jetpackrelated.databinding.recyclerDemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zxf.jetpackrelated.databinding.simpleUse.VideoEntity
import java.util.*

/**
 * 作者： zxf
 * 描述： dataBinding recyclerView 协助测试
 */
class DataBindingRecyclerViewActivity : AppCompatActivity() {

    lateinit var rvContent: RecyclerView

    private val data: MutableList<VideoEntity> = LinkedList()

    private val mAdapter: DataBindingRecyclerViewAdapter by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingRecyclerViewAdapter(data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(RecyclerView(this).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            it.overScrollMode = View.OVER_SCROLL_NEVER

            it.layoutManager = LinearLayoutManager(this)
            it.adapter = mAdapter
            rvContent = it
        })

        doTestData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun doTestData() {
        data.add(VideoEntity())
        data.add(
            VideoEntity(
                videoName = "盖亚奥特曼",
                videoImageUrl = "https://img1.baidu.com/it/u=461485760,3674946311&fm=26&fmt=auto"
            )
        )
        data.add(
            VideoEntity(
                videoName = "梦比优斯奥特曼",
                videoImageUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi2.hdslb.com%2Fbfs%2Farchive%2Fba13c376dbf0eeae7db6f745ebb08b49c686b7d1.png&refer=http%3A%2F%2Fi2.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1636533781&t=51479830a44244614da71744d103b739"
            )
        )
        data.add(
            VideoEntity(
                videoName = "欧布奥特曼",
                videoImageUrl = "https://img0.baidu.com/it/u=3048604516,1452719210&fm=26&fmt=auto"
            )
        )
        data.add(
            VideoEntity(
                videoName = "高斯奥特曼",
                videoImageUrl = "https://img2.baidu.com/it/u=1123299892,2833244789&fm=26&fmt=auto"
            )
        )
        data.add(VideoEntity())
        data.add(
            VideoEntity(
                videoName = "盖亚奥特曼",
                videoImageUrl = "https://img1.baidu.com/it/u=461485760,3674946311&fm=26&fmt=auto"
            )
        )
        data.add(
            VideoEntity(
                videoName = "梦比优斯奥特曼",
                videoImageUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi2.hdslb.com%2Fbfs%2Farchive%2Fba13c376dbf0eeae7db6f745ebb08b49c686b7d1.png&refer=http%3A%2F%2Fi2.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1636533781&t=51479830a44244614da71744d103b739"
            )
        )
        data.add(
            VideoEntity(
                videoName = "欧布奥特曼",
                videoImageUrl = "https://img0.baidu.com/it/u=3048604516,1452719210&fm=26&fmt=auto"
            )
        )
        data.add(
            VideoEntity(
                videoName = "高斯奥特曼",
                videoImageUrl = "https://img2.baidu.com/it/u=1123299892,2833244789&fm=26&fmt=auto"
            )
        )
        data.add(VideoEntity())
        data.add(
            VideoEntity(
                videoName = "盖亚奥特曼",
                videoImageUrl = "https://img1.baidu.com/it/u=461485760,3674946311&fm=26&fmt=auto"
            )
        )
        data.add(
            VideoEntity(
                videoName = "梦比优斯奥特曼",
                videoImageUrl = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi2.hdslb.com%2Fbfs%2Farchive%2Fba13c376dbf0eeae7db6f745ebb08b49c686b7d1.png&refer=http%3A%2F%2Fi2.hdslb.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1636533781&t=51479830a44244614da71744d103b739"
            )
        )
        data.add(
            VideoEntity(
                videoName = "欧布奥特曼",
                videoImageUrl = "https://img0.baidu.com/it/u=3048604516,1452719210&fm=26&fmt=auto"
            )
        )
        data.add(
            VideoEntity(
                videoName = "高斯奥特曼",
                videoImageUrl = "https://img2.baidu.com/it/u=1123299892,2833244789&fm=26&fmt=auto"
            )
        )
        mAdapter.notifyDataSetChanged()
    }


}