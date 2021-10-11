package com.zxf.jetpackrelated.databinding.simpleUse

import androidx.annotation.DrawableRes
import androidx.databinding.ObservableInt
import com.zxf.jetpackrelated.R

/**
 * 作者： zxf
 * 描述： VideoEntity 实体类
 */
data class VideoEntity(
    val videoName: String? = "迪迦·奥特曼",
    val videoIntroduction: String? = "《迪迦·奥特曼》（ウルトラマンティガ、Ultraman Tiga），是日本圆谷株式会社拍摄的特摄电视剧。是“平成系奥特曼”系列首作，平成三部曲的首作，是奥特曼系列自1980年的 《爱迪·奥特曼》后沉寂数年迎来的重生。于1996年（平成8年）9月7日至1997年（平成9年）8月30日在JNN日本新闻网播放 [1]  ，共52话。",
    val videoStarring: String? = "长野博；吉本多香美；高树零；增田由纪夫；影丸茂树；古屋畅一；川地民夫；石桥慧；二又一成",
    val videoImageUrl: String? = "https://img1.baidu.com/it/u=2288494528,306759139&fm=253&fmt=auto&app=120&f=JPEG?w=584&h=378",
    @DrawableRes val localImage: Int = R.drawable.ic_launcher_background,
    var paddingTestObservable: ObservableInt = ObservableInt(30),
    var videoRatingObservable: ObservableInt = ObservableInt(5)
) {
    /**
     * 只有xml文件中调用了Observable，之后产生的ActivityDatabindingBaseBindingImpl对应的代码赋值地方的flag不一样，才可以在data变化的时候，执行到数据的调用绑定。
     */
    fun convertRatingString(videoRating:Int): String {
        return when (videoRating) {
            1 -> "一星"
            2 -> "二星"
            3 -> "三星"
            4 -> "四星"
            5 -> "五星"
            else -> "零星"
        }
    }
    fun convertRatingString(): String {
        return when (videoRatingObservable.get()) {
            1 -> "一星"
            2 -> "二星"
            3 -> "三星"
            4 -> "四星"
            5 -> "五星"
            else -> "零星"
        }
    }
}