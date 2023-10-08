package com.pumpkin.applets_container

import android.os.Bundle
import com.pumpkin.applets_container.databinding.ActivityMain1Binding
import com.pumpkin.applets_container.view.MainFragment
import com.pumpkin.mvvm.util.UIHelper
import com.pumpkin.mvvm.view.BaseActivity
import com.pumpkin.pac.process.connectPool.GameConnectPool

/**
 * pumpkin
 *
 * 测试
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMain1Binding.inflate(layoutInflater).root)
        //主 fragment 设置
        UIHelper.showFragmentNoRemove(
            MainFragment::class.java.simpleName,
            supportFragmentManager,
            MainFragment(),
            R.id.fl_container
        )

        // 游戏进程预热测试 利用pac进程 爬虫
        GameConnectPool.connect()

//        binding.btGoTestMultiStateView.setOnClickListener {
//            startActivity(Intent(this@MainActivity, TestMultiStateViewActivity::class.java))
//        }
//        binding.btOtherTest.setOnClickListener {
//            GameHelper.openGame(
//                this@MainActivity, GameEntity(
//                    id = "ss",
//                    name = "test",
////                link = "https://narrow.one/",
//                    link = "https://gamesnacks.com/embed/gamesnacks/games/stackbounce?eids=44799761",
//                    icon = "",
//                    bigIcon = ""
//                )
//            )
//        }
//
//        binding.btTest.setOnClickListener {
////            Log.d(TAG, "onCreateAfter: ${
////                Gson().toJson(arrayListOf(GameEntity(
////                    id = "ss",
////                    name = "test",
////                    link = "https://gamesnacks.com/embed/gamesnacks/games/stackbounce?eids=44799761",
////                    describe = "",
////                    icon = "",
////                    bigIcon = "",
////                    extra = JsonObject().apply {
////                        addProperty("key1","value1")
////                        addProperty("key2","value2")
////                    }
////                )))
////            }")
//            // TODO: test fb data
//            binding.tvHelloWorld.text = KV.getDefaultModule(AppUtil.application)
//                .decodeString(FB.FB_HOME_BIG_FEED_DATA, "no data")
//        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}