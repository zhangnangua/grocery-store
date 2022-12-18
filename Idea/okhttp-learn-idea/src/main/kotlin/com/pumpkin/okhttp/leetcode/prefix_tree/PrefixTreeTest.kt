package com.pumpkin.okhttp.leetcode.prefix_tree

import java.lang.StringBuilder

fun main() {

    val prefixTree = PrefixTree()

    prefixTree.insert("hello")
    prefixTree.insert("helloworld")
    prefixTree.insert("hellohello")
    prefixTree.insert("hi")
    prefixTree.insert("in")
    prefixTree.insert("inn")

    println(prefixTree.search("i"))
    println(prefixTree.search("in"))
    println(prefixTree.search("hello"))
    println(prefixTree.search("hell"))
    println(prefixTree.search("do"))

    println(prefixTree.startWith("hell"))

    println("前缀查找测试 -- ${prefixTree.matchingStartWith("hel")?.joinToString("、")}")
    println("前缀查找测试 -- ${prefixTree.matchingStartWith("c")?.joinToString("、")}")

    println("end")
}


class PrefixTree {
    private val root = Node()

    fun matchingStartWith(word: String): List<String>? {
        val wordResultPre = StringBuilder()
        var localRoot = root
        word.toCharArray().forEach { char: Char ->
            val index = char - 'a'
            if (localRoot.childNode[index] != null) {
                wordResultPre.append(char)
                localRoot = localRoot.childNode[index]!!
            } else {
                return null
            }
        }
        //深度优先搜索 所有的剩余单词
        val result = ArrayList<String>()
        dfs(localRoot, StringBuilder(),wordResultPre,result)
        return result
    }

    private fun dfs(node: Node, str: StringBuilder, wordResultPre: StringBuilder, result: ArrayList<String>) {
        val childNodes = node.childNode
        for (index in childNodes.indices) {
            val childNode = childNodes[index]
            if (childNode != null) {
                str.append(('a' + index).toChar())
                if (childNode.isWord) {
                    result.add(wordResultPre.toString() + str.toString())
                }
                dfs(childNode,str, wordResultPre, result)
            }
        }
    }

    /**
     * 如果前缀树中存在包含该字符串的前缀，则返回true，否则返回false
     */
    fun startWith(word: String): Boolean {
        var localRoot = root
        word.toCharArray().forEach { char: Char ->
            val index = char - 'a'
            if (localRoot.childNode[index] != null) {
                localRoot = localRoot.childNode[index]!!
            } else {
                return false
            }
        }
        return true
    }

    /**
     * 查找 如果前缀树中存在该字符串，则返回true
     */
    fun search(word: String): Boolean {
        var localRoot = root
        word.toCharArray().forEach { char: Char ->
            val index = char - 'a'
            if (localRoot.childNode[index] != null) {
                localRoot = localRoot.childNode[index]!!
            } else {
                return false
            }
        }
        return localRoot.isWord
    }

    /**
     * 插入
     */
    fun insert(word: String) {
        var localRoot = root
        word.toCharArray().forEach { char ->
            val index = char - 'a'
            if (localRoot.childNode[index] == null) {
                localRoot.childNode[index] = Node()
            }
            localRoot = localRoot.childNode[index]!!
        }
        localRoot.isWord = true
    }

    class Node {
        val childNode = Array<Node?>(26) { null }
        var isWord = false
    }
}