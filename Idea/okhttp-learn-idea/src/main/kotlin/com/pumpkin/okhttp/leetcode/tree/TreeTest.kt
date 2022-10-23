package com.pumpkin.okhttp.leetcode.tree

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
    binaryTreeBreadth(Helper.createBinaryTree()).forEach {
        println(it)
    }
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
}

class TreeNode(var value: String, var leftNode: TreeNode? = null, var rightNode: TreeNode? = null)