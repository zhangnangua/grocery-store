package com.pumpkin.okhttp.leetcode.sort

import com.pumpkin.okhttp.leetcode.bean.ListNode
import java.util.*
import kotlin.math.min
import kotlin.random.Random

fun main() {
    //计数排序
    val array = intArrayOf(2, 3, 4, 2, 3, 2, 1)
    countSortArray(array)
    println("计数排序 ${array.joinToString()}")

    val arr1 = intArrayOf(2, 3, 3, 7, 3, 9, 2, 1, 7, 2)
    countSortArrayQuestion(arr1, intArrayOf(3, 2, 1))
    println(arr1.joinToString())

    //快速排序
    val array1 = intArrayOf(2, 3, 4, 2, 3, 2, 1)
    fastSortArray(array1)
    println("快速排序 ${array1.joinToString()}")

    //快速排序 搜索第K大的值
    val array2 = intArrayOf(3, 1, 2, 4, 5, 5, 6)
    val kthLargestValue = kthLargestValue(array2, 3)
    println("第三位置的值为 $kthLargestValue")
    val kthLargestValueByPartition = kthLargestValueByPartition2(array2, 3)
    println("第三位置的值为 $kthLargestValueByPartition")

    //归并排序
    val array3 = intArrayOf(2, 3, 4, 2, 3, 2, 1)
    println("归并排序 ${mergeIntoSort4(array3).joinToString()}")

    //归并排序 链表排序  3 5 1 4 2 6
    val node = ListNode().apply {
        value = 3
        next = ListNode().apply {
            value = 5
            next = ListNode().apply {
                value = 1
                next = ListNode().apply {
                    value = 4
                    next = ListNode().apply {
                        value = 2
                        next = ListNode().apply {
                            value = 6
                        }
                    }
                }
            }
        }
    }
    var sortListNodeByMergeInto = sortListNodeByMergeInto(node)
    var result = ArrayList<Int>()
    while (sortListNodeByMergeInto != null) {
        result.add(sortListNodeByMergeInto.value)
        sortListNodeByMergeInto = sortListNodeByMergeInto.next
    }
    println("链表排序结果 ${result.joinToString(",")}")

}

/**
 * 计数排序
 */
fun countSortArray(array: IntArray) {
    var maxValue = Int.MIN_VALUE
    var minValue = Int.MAX_VALUE
    array.forEach {
        maxValue = Math.max(maxValue, it)
        minValue = Math.min(minValue, it)
    }

    val snapArray = IntArray(maxValue - minValue + 1)
    array.forEach {
        val index = it - minValue
        val snapCount = snapArray[index]
        snapArray[index] = snapCount + 1
    }

    var index = 0
    for (i in snapArray.indices) {
        val value = snapArray[i]
        var count = value
        while (count > 0) {
            array[index++] = i + minValue
            count--
        }
    }
}

fun countSortArrayQuestion(array1: IntArray, array2: IntArray) {
    val diffValue = 1000
    val snapArray = IntArray(diffValue + 1)
    array1.forEach {
        val index = it
        val snapCount = snapArray[index]
        snapArray[index] = snapCount + 1
    }

    var index = 0
    array2.forEach {
        val value = snapArray[it]
        var count = value
        while (count > 0) {
            array1[index++] = it
            count--
        }
        snapArray[it] = 0
    }

    for (i in snapArray.indices) {
        val value = snapArray[i]
        var count = value
        while (count > 0) {
            array1[index++] = i
            count--
        }
    }
}

/**
 * 快速排序
 */
fun fastSortArray(array: IntArray) {
    fastSortArray(array, 0, array.size - 1)
}

fun fastSortArray(array: IntArray, start: Int, end: Int) {
    if (end > start) {
        val pivot = partition(start, end, array)
        //pivot 支点 已排序的位置已经固定 所以可以越过它 继续排序
        fastSortArray(array, start, pivot - 1)
        fastSortArray(array, pivot + 1, end)
    }
}

/**
 * 分区
 */
fun partition(start: Int, end: Int, array: IntArray): Int {
    //如果用相同的种子创建两个 Random 实例，则对每个实例进行相同的方法调用序列，它们将生成并返回相同的数字序列
    //nextInt 返回从该随机数生成器的序列中提取的介于0（含）和指定值（不含）之间的伪随机、均匀分布的int值. 故 加一
    val randomPivot = Random(System.currentTimeMillis()).nextInt(end - start + 1) + start
    //将该值交换到数组末尾
    swap(array, randomPivot, end)

    var small = start - 1
    for (i in start until end) {
        if (array[i] < array[end]) {
            small++
            swap(array, small, i)
        }
    }

    small++
    swap(array, small, end)

    return small
}

fun swap(array: IntArray, position1: Int, position2: Int) {
    if (position1 != position2) {
        val temporary = array[position1]
        array[position1] = array[position2]
        array[position2] = temporary
    }
}

/**
 * 搜索第k个大的值
 */
fun kthLargestValue(array: IntArray, k: Int): Int {
    val minPriorityQueue = PriorityQueue<Int>()
    array.forEach {
        if (minPriorityQueue.size < k) {
            minPriorityQueue.add(it)
        } else if (minPriorityQueue.peek() < it) {
            minPriorityQueue.poll()
            minPriorityQueue.add(it)
        }
    }
    return minPriorityQueue.peek()
}

/**
 * 查找第k大的值，通过分区的方式
 */
fun kthLargestValueByPartition(array: IntArray, k: Int): Int {
    val kIndex = array.size - k
    return kthLargestValueByPartition(0, array.size - 1, array, kIndex)

}

private fun kthLargestValueByPartition2(array: IntArray, k: Int): Int {
    val kIndex = array.size - k
    var start = 0
    var end = array.size - 1
    var index = partition(start, end, array)
    while (kIndex != index) {
        if (kIndex > index) {
            start = index + 1
        } else {
            end = index - 1
        }
        index = partition(start, end, array)
    }
    return array[index]
}

private fun kthLargestValueByPartition(start: Int, end: Int, array: IntArray, kIndex: Int): Int {
    val partition = partition(start, end, array)
    return if (partition == kIndex) {
        array[partition]
    } else if (partition < kIndex) {
        kthLargestValueByPartition(partition + 1, end, array, kIndex)
    } else {
        kthLargestValueByPartition(start, partition - 1, array, kIndex)
    }
}

/**
 * 归并排序
 */
fun mergeIntoSort(array3: IntArray): IntArray {

    val length = array3.size
    var src = array3
    var dst = IntArray(length)

    var seg = 1
    while (seg < length) {
        var start = 0
        while (start < length) {
            var mid = (start + seg).coerceAtMost(length)
            var end = (start + seg * 2).coerceAtMost(length)

            var i = start
            var j = mid
            var k = start
            while (i < mid || j < end) {
                if (j == end || (i < mid && src[i] < src[j])) {
                    dst[k++] = src[i++]
                } else {
                    dst[k++] = src[j++]
                }
            }

            start += seg * 2
        }
        //交换的目的 是让dst和src不指向同一个数据 避免互相影响
        val temp = src
        src = dst
        dst = temp

        seg += seg
    }

    return src
}


fun mergeIntoSort2(array: IntArray): IntArray {
    val length = array.size
    var src = array
    var dst = IntArray(length)

    var seg = 1
    while (seg < length) {
        var start = 0
        while (start < length) {
            val mid = (start + seg).coerceAtMost(length)
            val end = (start + seg * 2).coerceAtMost(length)
            var i = start
            var j = mid
            var k = start

            while (i < mid || j < end) {
                if (j == end || (src[i] < src[j] && i < mid)) {
                    dst[k++] = src[i++]
                } else {
                    dst[k++] = src[j++]
                }
            }
            start += seg * 2
        }
        val temp = src
        src = dst
        dst = temp

        seg += seg
    }
    return src
}

/**
 * 归并排序的递归写法
 */
fun mergeIntoSort3(array: IntArray): IntArray {
    val src = array
    val dst = array.copyOf()
    mergeIntoSort3(src, dst, 0, array.size)
    return dst
}

private fun mergeIntoSort3(src: IntArray, dst: IntArray, start: Int, end: Int) {
    if (start + 1 >= end) {
        return
    }

    val mid = (start + end) / 2
    mergeIntoSort3(dst, src, start, mid)
    mergeIntoSort3(dst, src, mid, end)

    var i = start
    var j = mid
    var k = start
    while (i < mid || j < end) {
        if (j == end || (i < mid && src[i] < src[j])) {
            dst[k++] = src[i++]
        } else {
            dst[k++] = src[j++]
        }
    }

}

fun mergeIntoSort4(array: IntArray): IntArray {
    val length = array.size
    var src = array
    var dst = IntArray(length)
    //合并相邻序列的数组，从1开始
    var sequence = 1
    while (sequence < length) {
        //循环数组 按照序列进行 子数组排序
        var start = 0
        while (start < length) {
            val middle = min(sequence + start, length)
            val end = min(sequence * 2 + start, length)
            var i = start
            var j = middle
            var k = start
            //字数组 具体的排序过程
            while (i < middle || j < end) {
                if (j == end || (i < middle && src[i] < src[j])) {
                    dst[k++] = src[i++]
                } else {
                    dst[k++] = src[j++]
                }
            }
            start += sequence * 2
        }
        //交换数组 主要是防止真正的排序是 dst和src指向同一个数组
        val temp = src
        src = dst
        dst = temp
        //下一个序列
        sequence += sequence
    }
    return src
}

/**
 * 以归并的方式 排序链表
 */
fun sortListNodeByMergeInto(node: ListNode?): ListNode? {
    if (node == null || node.next == null) {
        return node
    }

    val middleNode = findMiddleNodeAndSplit(node)
    val node1 = sortListNodeByMergeInto(node)
    val node2 = sortListNodeByMergeInto(middleNode)

    return merge(node1, node2)
}

fun merge(node1: ListNode?, node2: ListNode?): ListNode? {
    val dummy = ListNode()
    var newNode: ListNode? = dummy
    var leftNode = node1
    var rightNode = node2
    while (leftNode != null && rightNode != null) {
        if (leftNode.value < rightNode.value) {
            newNode!!.next = leftNode
            leftNode = leftNode.next
        } else {
            newNode!!.next = rightNode
            rightNode = rightNode.next
        }
        newNode = newNode.next
    }
    newNode!!.next = leftNode ?: rightNode
    return dummy.next
}

/**
 * 以快慢指针的形式寻找中间节点  并分离节点
 */
fun findMiddleNodeAndSplit(node: ListNode): ListNode? {
    var slowPointer: ListNode? = node
    var quickPointer: ListNode? = node.next
    while (quickPointer != null && quickPointer.next != null) {
        slowPointer = slowPointer?.next
        quickPointer = quickPointer.next!!.next
    }

    val second = slowPointer?.next
    slowPointer?.next = null

    return second
}





















