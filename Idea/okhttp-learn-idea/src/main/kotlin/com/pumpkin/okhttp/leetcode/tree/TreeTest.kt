package com.pumpkin.okhttp.leetcode.tree

fun main() {
    //前序遍历
    binaryTreePreIterator(Helper.createBinaryTree())
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
fun binaryTreePreIteratorByStack(node: TreeNode?) {
    if (node != null) {
        println(node.value)
        binaryTreePreIterator(node.leftNode)
        binaryTreePreIterator(node.rightNode)
    }
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