package com.pumpkin.okhttp.leetcode.array

import java.util.*

fun main() {
    // TODO: 2022/4/17 1.排序数组中两个数字之和
    println(twoSum(intArrayOf(1, 2, 4, 6, 10), 8).joinToString())
    // TODO: 2022/4/18 2.数组中和为 0 的三个数
    println(threeSum(intArrayOf(-1, 0, 1, 2, -1, -4)).joinToString())
    // TODO: 2022/5/2  3.和大于等于 target 的最短子数组
    println(minSubArrayLen(7, intArrayOf(1, 1, 1, 1, 7)))
    println(minSubArrayLenByBook(7, intArrayOf(1, 1, 1, 1, 7)))
    // TODO: 2022/5/3 乘积小于k的子数组
    println(numSubArrayProductLessThank(intArrayOf(10, 5, 2, 6), 100))
    // TODO: 2022/5/3 和为k的子数组

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
//            log("i:$i p1:$p1 p2:$p2")
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

/**
 * 自己写的通向指针版本
 *
 * 给定一个含有 n 个正整数的数组和一个正整数 target
 * 找出该数组中满足其和 ≥ target 的长度最小的 连续子数组[numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长度。如果不存在符合条件的子数组，返回 0
 * @param target 正整数target
 * @param nums 正整数数组
 */
fun minSubArrayLen(target: Int, nums: IntArray): Int {
    if (nums.isEmpty()) return 0

    //由于是正整数数组，故可以利用双指针，同向指针锁定数组
    var result = 0
    //指针p1,p2
    var p1 = 0
    var p2 = 0
    //当前双指针之间的数组之和
    var sum = nums[p2]
    while (p1 <= p2) {
        //注意临界条件 p2 < nums.size - 1
        while (sum < target && p2 < nums.size - 1) {
            p2++
            sum += nums[p2]
        }
        //注意临界条件 p1 < nums.size
        while (sum >= target && p1 < nums.size) {
            //数组之间的差值
            result = if (result == 0) {
                p2 - p1 + 1
            } else {
                Math.min(p2 - p1 + 1, result)
            }

            //p1 右侧移动
            sum -= nums[p1]
            p1++
        }

        if (p2 == nums.size - 1) {
            return result
        }
    }

    return result
}

/**
 * 书上写的版本
 *
 * 给定一个含有 n 个正整数的数组和一个正整数 target
 * 找出该数组中满足其和 ≥ target 的长度最小的 连续子数组[numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长度。如果不存在符合条件的子数组，返回 0
 * @param target 正整数target
 * @param nums 正整数数组
 */
fun minSubArrayLenByBook(target: Int, nums: IntArray): Int {
    //一样利用通向双指针
    var leftPoint = 0
    var sum = 0
    var result = Int.MAX_VALUE

    for (rightPoint in nums.indices) {
        sum += nums[rightPoint]
        while (sum >= target && rightPoint >= leftPoint) {
            result = Math.min(result, rightPoint - leftPoint + 1)
            sum -= nums[leftPoint]
            leftPoint++
        }
    }

    return if (result == Int.MAX_VALUE) 0 else result
}

/**
 * 给定一个正整数数组 nums和整数 k ，请找的出该数组内乘积小于 k 的连续子数组的个数
 * @param nums 正整数数组
 * @param k k
 */
fun numSubArrayProductLessThank(nums: IntArray, k: Int): Int {
    //利用双指针
    var result = 0

    var sum = 1
    var leftPoint = 0
    for (rightPoint in nums.indices) {
        sum *= nums[rightPoint]
        while (leftPoint <= rightPoint && sum >= k) {
            sum /= nums[leftPoint]
            leftPoint++
        }
        //比如到abc [c bc abc]    abcd [d cd bcd abcd]
        result += rightPoint - leftPoint + 1
    }

    return result
}

/**
 * 给定一个整数数组和一个整数 k ，请找到该数组中和为 k 的连续子数组的个数。
 *
 * @param nums 整数数组
 * @param k 整数
 *
 */
fun subArraySum(nums: IntArray, k: Int): Int {
    /**
     * 思路浅析
     * 如果是正整数数组，查找一个数，可以先排序利用逆向双指针查找；查找子数组，可以用同向双指针。
     * 没用说是正整数数组，故不能直接使用双指针，首先需要知道一个概念，在一个整数数组中，S0记为下标到0的和，S1记为下标到1的和，S2记为下标到2的和...   则下标3到下标6的和，则为S6-S2 。
     * 所以我们可以扫描所有的从下标0到对应下标i的和，在扫描过程中如果存在j（j<i）Si-Sj=k，则i+1 - j则是我们需要找的连续子数组。
     *
     * 最终可以使用一个hash表，key为从下标0到下标i的和，value 为该值出现的次数。
     */
    val result = 0
    val sumToCount = HashMap<Int, Int>()



    return result
}


fun test(){

}















