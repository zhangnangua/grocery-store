package com.pumpkin.okhttp.leetcode.sort

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
    fastSortArray(array1, 0, array1.size - 1)
    println("快速排序 ${array1.joinToString()}")
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
