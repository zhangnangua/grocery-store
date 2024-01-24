package com.pumpkin.webCache.domain.matcher

import android.util.SparseArray
import com.pumpkin.webCache.domain.ReversibleString
import com.pumpkin.webCache.domain.reverse

/**
 * 实现简单的前缀树 , 用于实现索引url , 默认反转字符串储存
 */
open class Trie {
    private val children = SparseArray<Trie>()
    private var terminator: Boolean = false

    fun findNode(s: String): Trie? = findNode(s.reverse())

    private fun findNode(s: ReversibleString): Trie? {
        if (terminator && (s.length() == 0 || s.chatAt(0) == '.')) {
            //wo do not want to return on partial matches , If the trie node is bar.com,
            //and the search string is foo-bar.com , we should not match ,
            //but foo.bar.com should match.
            return this
        }
        val c = s.chatAt(0)
        return children.get(c.code)?.findNode(s.substring(1))
    }

    open fun put(s: String): Trie = put(s.reverse())

    open fun put(s: ReversibleString): Trie {
        if (s.length() == 0) {
            terminator = true
            return this
        }
        val char = s.chatAt(0)
        val trie = put(char)
        return trie.put(s.substring(1))
    }

    open fun put(c: Char): Trie {
        val existChild = children.get(c.code)
        if (existChild != null) {
            return existChild
        }
        val newChild = createNode()
        children.put(c.code, newChild)
        return newChild
    }


    private fun createNode() = Trie()

    companion object {
        fun createRootNode() = Trie()
    }

}