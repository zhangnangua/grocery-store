package com.pumpkin.parse

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

/**
 * ParsingEntry
 *
 * 利用webView 解析 然后注入爬虫代码进行解析  持有一个webView 爬去 标签 分类  存到数据
 */
class ParseEntrance {

    @Throws(IOException::class)
    fun entrance(url: String): Document? {
        return Jsoup.connect(url).get()
    }
}