package com.pumpkin.okhttp.leetcode.tree

import java.util.TreeMap

fun main() {
    //前序遍历
//    binaryTreePreIterator(Helper.createBinaryTree())
//    binaryTreePreIteratorByStack(Helper.createBinaryTree()).forEach {
//        println(it)
//    }
    //中序遍历
//    binaryTreeMiddleIterator(Helper.createBinaryTree())
//    binaryTreeMiddleIteratorByStack(Helper.createBinaryTree()).forEach {
//        println(it)
//    }
    //后序遍历
//    binaryTreeAfterIterator(Helper.createBinaryTree())
//    binaryTreeAfterIteratorByStack(Helper.createBinaryTree()).forEach {
//        println(it)
//    }

    //广度优先搜索
//    binaryTreeBreadth(Helper.createBinaryTree()).forEach {
//        println(it)
//    }

    //二叉搜索树 查找值 O(h)
//    binarySearchTreeFind(Helper.createBinarySearchTree(), 9).let {
//        println("value is ${it?.value ?: "is null"}")
//    }

    //日程表
    val calendar = MyCalendar()
    println(calendar.book(10, 20))
    println(calendar.book(15, 25))
    println(calendar.book(20, 30))
}

/**
 * 前序遍历  根左右
 * 递归写法
 */
fun binaryTreePreIterator(node: TreeNode?) {
    if (node != null) {
        println(node.value)
        binaryTreePreIterator(node.leftNode)
        binaryTreePreIterator(node.rightNode)
    }
}

/**
 * 前序遍历  根左右
 * 非递归写法
 */
fun binaryTreePreIteratorByStack(node: TreeNode?): ArrayList<String> {
    val result = ArrayList<String>()

    val stack = java.util.ArrayDeque<TreeNode>()
    var currentNode = node;
    while (currentNode != null || !stack.isEmpty()) {
        while (currentNode != null) {
            result.add(currentNode.value)
            stack.push(currentNode)
            currentNode = currentNode.leftNode
        }
        currentNode = stack.pop().rightNode
    }

    return result
}

/**
 * 中序遍历
 * 左根右
 */
fun binaryTreeMiddleIterator(node: TreeNode?) {
    if (node != null) {
        binaryTreeMiddleIterator(node.leftNode)
        println(node.value)
        binaryTreeMiddleIterator(node.rightNode)
    }
}

/**
 * 中序遍历  非递归写法
 * 左根右
 */
fun binaryTreeMiddleIteratorByStack(node: TreeNode?): ArrayList<String> {
    val result = ArrayList<String>()

    val stack = java.util.ArrayDeque<TreeNode>()
    var currentNode = node
    while (currentNode != null || !stack.isEmpty()) {
        while (currentNode != null) {
            stack.push(currentNode)
            currentNode = currentNode.leftNode
        }
        val treeNode = stack.pop()
        result.add(treeNode.value)
        currentNode = treeNode.rightNode
    }

    return result
}

/**
 * 后序遍历
 * 左右根
 */
fun binaryTreeAfterIterator(node: TreeNode?) {
    if (node != null) {
        binaryTreeAfterIterator(node.leftNode)
        binaryTreeAfterIterator(node.rightNode)
        println(node.value)
    }
}

/**
 * 后序遍历 非递归写法
 * 左右根
 */
fun binaryTreeAfterIteratorByStack(node: TreeNode?): ArrayList<String> {
    val result = ArrayList<String>()

    val stack = java.util.ArrayDeque<TreeNode>()
    var currentNode: TreeNode? = node
    var preNode: TreeNode? = null
    while (currentNode != null || !stack.isEmpty()) {
        while (currentNode != null) {
            stack.push(currentNode)
            currentNode = currentNode.leftNode
        }
        currentNode = stack.peek()
        if (currentNode.rightNode != null && currentNode.rightNode != preNode) {
            //遍历右子树
            currentNode = currentNode.rightNode
        } else {
            stack.pop()
            result.add(currentNode.value)
            preNode = currentNode
            currentNode = null
        }
    }

    return result
}

/**
 * 广度优先搜索   使用双队列解决
 */
fun binaryTreeBreadth(node: TreeNode): ArrayList<String> {
    val result = ArrayList<String>()

    val arrayDeque1 = java.util.ArrayDeque<TreeNode>()
    val arrayDeque2 = java.util.ArrayDeque<TreeNode>()
    arrayDeque1.offer(node)
    while (!arrayDeque1.isEmpty() || !arrayDeque2.isEmpty()) {
        while (!arrayDeque1.isEmpty()) {
            val treeNode = arrayDeque1.poll()
            if (treeNode != null) {
                result.add(treeNode.value)
                val leftNode = treeNode.leftNode
                if (leftNode != null) {
                    arrayDeque2.offer(leftNode)
                }
                val rightNode = treeNode.rightNode
                if (rightNode != null) {
                    arrayDeque2.offer(rightNode)
                }
            }
        }
        while (!arrayDeque2.isEmpty()) {
            val treeNode = arrayDeque2.poll()
            if (treeNode != null) {
                result.add(treeNode.value)
                val leftNode = treeNode.leftNode
                if (leftNode != null) {
                    arrayDeque1.offer(leftNode)
                }
                val rightNode = treeNode.rightNode
                if (rightNode != null) {
                    arrayDeque1.offer(rightNode)
                }
            }
        }
    }

    return result
}

/**
 * 二叉搜索树，查找值，时间复杂度O(log(n))
 */
fun binarySearchTreeFind(node: TreeNode1, value: Int): TreeNode1? {
    var currentNode: TreeNode1? = node;
    while (currentNode != null) {
        if (currentNode.value == value) {
            break
        }
        if (currentNode.value > value) {
            currentNode = currentNode.leftNode
        } else {
            currentNode = currentNode.rightNode
        }
    }
    return currentNode
}


object Helper {

    /**
     * 创建二叉树
     */
    fun createBinaryTree(): TreeNode {
        return TreeNode(
            value = "A",
            leftNode = TreeNode(
                value = "B",
                leftNode = TreeNode(value = "D"),
                rightNode = TreeNode(value = "E")
            ),
            rightNode = TreeNode(
                value = "C",
                leftNode = TreeNode(value = "F"),
                rightNode = TreeNode(value = "G")
            )
        )
    }

    /**
     * 简单二叉搜索树创建
     */
    fun createBinarySearchTree(): TreeNode1 {
        return TreeNode1(
            value = 7,
            leftNode = TreeNode1(
                value = 5,
                leftNode = TreeNode1(value = 4),
                rightNode = TreeNode1(value = 6)
            ),
            rightNode = TreeNode1(
                value = 9,
                leftNode = TreeNode1(value = 8),
                rightNode = TreeNode1(value = 10)
            )
        )
    }
}

class TreeNode(var value: String, var leftNode: TreeNode? = null, var rightNode: TreeNode? = null)

class TreeNode1(var value: Int, var leftNode: TreeNode1? = null, var rightNode: TreeNode1? = null)


/**
 * 利用TreeMap实现日程表
 */
class MyCalendar {

    /**
     * key : start
     * value : end
     */
    private val events = TreeMap<Int, Int>()

    fun book(start: Int, end: Int): Boolean {

        val event = events.floorEntry(start)
        if (event != null && event.value > start) {
            return false
        }

        val event2 = events.ceilingEntry(start)
        if (event2 != null && event2.key < end) {
            return false
        }

        //存入
        events[start] = end
        return true
    }

}









