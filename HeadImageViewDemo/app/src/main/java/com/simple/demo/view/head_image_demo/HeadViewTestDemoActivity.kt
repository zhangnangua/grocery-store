package com.simple.demo.view.head_image_demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simple.demo.databinding.ActivityHeadViewDemoBinding
import java.util.*

/**
 * 作者： zxf
 * 描述： HeadViewTestDemo
 */
class HeadViewTestDemoActivity : AppCompatActivity() {
    lateinit var binding: ActivityHeadViewDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCreate()
    }

    private fun initCreate() {
        binding = ActivityHeadViewDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.rvContent) {
            layoutManager = LinearLayoutManager(this@HeadViewTestDemoActivity)
            adapter = ItemAdapter(this@HeadViewTestDemoActivity).apply {
                datas = obtainData()
            }
        }
    }

    /**
     * 测试数据
     */
    private fun obtainData(): List<Pair<String, String>> {
        return LinkedList<Pair<String, String>>().apply {
            for (i in 0..50) {
                add(
                    Pair(
                        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201703%2F02%2F20170302192447_wQfrh.thumb.700_0.jpeg&refer=http%3A%2F%2Fb-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630058227&t=e165c8434bc502c04f2b3b19fefff7f9",
                        "无敌迪迦"
                    )
                )
                add(
                    Pair(
                        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn10109%2F329%2Fw640h489%2F20200228%2F283c-ipzreiw9657407.jpg&refer=http%3A%2F%2Fn.sinaimg.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630049523&t=ed503fae8f9341b2251ca2e97e001480",
                        "泰迦新形态：泰迦"
                    )
                )
                add(
                    Pair(
                        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.baike.soso.com%2Fugc%2Fbaikepic2%2F938%2F20180515202850-89579340_jpeg_958_539_441005.jpg%2F0&refer=http%3A%2F%2Fpic.baike.soso.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1630049523&t=f3142221e3efa3e3857fb4f3eb23e552",
                        "奥特曼·迪迦：迪迦"
                    )
                )
                add(Pair("errorurl", "图片加载失败：占位"))
            }
        }
    }
}