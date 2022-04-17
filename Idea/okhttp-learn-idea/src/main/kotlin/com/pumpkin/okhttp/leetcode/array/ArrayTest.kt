package com.pumpkin.okhttp.leetcode.array

fun main() {
    // TODO: 2022/4/17 1.排序数组中两个数字之和
    println(twoSum(intArrayOf(1, 2, 4, 6, 10), 8).joinToString ())
}

/**
 * 给定一个已按照 升序排列  的整数数组 numbers ，请你从数组中找出两个数满足相加之和等于目标数 target 。假设数组中存在且只存在一对符合条件的数字，同时一个数字不能使用两次。
 * @param numbers 升序数组
 * @param target 目标值
 */
fun twoSum(numbers: IntArray, target: Int): IntArray {
    //若普通的暴力作法，时间复杂度 N^2
    //利用方向相反的双指针来做，一个指针`P1`指向数组的第`1`个数字，另一个指针`P2`指向数组的最后一个数字，然后比较两个指针指向的数字之和及一个目标值。
    // 如果两个指针指向的数字之和大于目标值，则向左移动指针`P2`；如果两个指针指向的数字之和小于目标值，则向右移动指针`P1`。

    //时间复杂度n，空间复杂度n
    val result = IntArray(2)

    var p1 = 0
    var p2 = numbers.size - 1
    while (p2 >= p1) {
        val snapTarget = numbers[p1] + numbers[p2]
        if (target == snapTarget) {
            //找到结果，退出
            result[0] = p1
            result[1] = p2
            break
        } else if (target > snapTarget) {
            p1++
        } else {
            p2--
        }
    }
    return result
}