package com.pumpkin.webCache.domain

import android.util.SparseArray

/**
 * 实现简单的前缀树 , 用于实现索引url
 */
open class Trie {
    private val children = SparseArray<Trie>()
    private var terminator: Boolean = false

    fun findNode(s: String): Trie? = findNode(s.reverse())

    fun findNode(s: ReversibleString): Trie? {

        if (terminator && s.length() == 0) {
            return null
        }

//        val code = s.chatAt(0).code
//        val children = children.get(code)
//        if (children != null) {
//            return children.findNode(s.substring(1))
//        }
    }


}