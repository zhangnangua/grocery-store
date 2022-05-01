package com.pumpkin.okhttp.leetcode.array

import com.pumpkin.okhttp.util.log
import java.util.*

fun main() {
    // TODO: 2022/4/17 1.排序数组中两个数字之和
    println(twoSum(intArrayOf(1, 2, 4, 6, 10), 8).joinToString())
    // TODO: 2022/4/18 2.数组中和为 0 的三个数
    println(threeSum(intArrayOf(-1, 0, 1, 2, -1, -4)).joinToString())
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

/**
 * 给定一个包含 n 个整数的数组nums，判断nums中是否存在三个元素a ，b ，c ，使得a + b + c = 0 ？请找出所有和为 0 且不重复的三元组。
 * @param nums n个整数的数组
 */
fun threeSum(nums: IntArray): List<List<Int>> {
    val result = ArrayList<List<Int>>()

    if (nums.size < 3) {
        return result
    }
    //上方已经找到在一个排序的数组中查找两个数的和为n，所以需要首先排序，之后提前固定一个数，计算出所需的target，然后利用两指针反向查找target即可。时间总复杂度为n^2
    //排序所需的时间复杂度为 nloogn ，
    //其中temp系列变量用于跳过重复的值

    //排序
    Arrays.sort(nums)
    //固定一个值
    var i = 0
    while (i < nums.size - 2) {
        //双指针查找
        val target = 0 - nums[i]
        var p1 = i + 1
        var p2 = nums.size - 1
        while (p2 > p1) {
            log("i:$i p1:$p1 p2:$p2")
            if (nums[p1] + nums[p2] == target) {
                //加入集合
                result.add(ArrayList<Int>().apply {
                    add(nums[i])
                    add(nums[p1])
                    add(nums[p2])
                })

                //跳过目标值，继续寻找下一组
                val tempP1 = nums[p1]
                while (tempP1 == nums[p1] && p1 < p2) {
                    p1++
                }

            } else if (nums[p1] + nums[p2] < target) {
                p1++
            } else {
                p2--
            }
        }

        //跳过相同的i
        val tempI = nums[i]
        while (tempI == nums[i] && i < nums.size - 2) {
            i++
        }
    }

    return result
}