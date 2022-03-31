package com.pumpkin.okhttp.leetcode.binary_byte

/**
 * 二进制相关算法测试
 */
fun main() {
    //二进制 基础左移右移测试
//    basicTest()

    //todo 算法题
    //todo 1.二进制加法计算
    println(binaryAdd("11011", "111110011"))

    //todo 2.前n个数字二进制中1的个数
    println(countBitsCase1(4).joinToString())  //时间复杂度 O(nk)
    println(countBitsCase2(4).joinToString())  //时间复杂度 O(n)
    println(countBitsCase3(4).joinToString())  //时间复杂度 O(n)

    //todo 3.只出现一次的数字
    println(singleNumber(arrayOf(2, 2, 3, 2).toIntArray()))

    //todo 4.单词长度的最大乘积
    println(maxProduct1(arrayOf("abcw", "foo", "bar", "fxyz", "abcdef")))
    println(maxProduct2(arrayOf("abcw", "foo", "bar", "fxyz", "abcdef")))
}

/**
 * 输入一个字符串数组words，请计算不包含相同字符的两个字符串words[i]和words[j]的长度乘积的最大值。如果所有字符串都包含至少一个相同字符，那么返回0。假设只包含小写字母
 * @param words 数组
 */
fun maxProduct1(words: Array<String>): Int {
    // 解决这个问题的关键在于如何判断两个字符串str1和str2中没有相同的字符。暴力循环比对的话时间复杂度是O(pq)
    // 可以用哈希表来优化时间效率。对于每个字符串，可以用一个哈希表记录出现在该字符串中的所有字符。
    // 在判断两个字符串str1和str2中是否有相同的字符时，只需要从'a'到'z'判断某个字符是否在两个字符串对应的哈希表中都出现了。

    //因为英文字母是26个，故需要26分别进行对应。若当前的位置包含改字符，则将改位置设置为true。
    val hashTable = Array(words.size) { Array(26) { false } }

    //将每个单词对应的字母分别映射到 hash表中
    for (i in words.indices) {
        val str = words[i]
        str.forEach { char: Char ->
            hashTable[i][char - 'a'] = true
        }
    }

    //判断是否存在最大值
    var maxValue = 0
    for (i in words.indices) {
        for (j in (i + 1) until words.size) {
            var flag: Byte = 0
            for (c in 0..25) {
                //如果存在相同的
                if (hashTable[i][c] && hashTable[j][c]) {
                    flag = 1
                    break
                }
            }
            if (flag == 0.toByte()) {
                maxValue = Math.max(maxValue, words[i].length * words[j].length)
            }
        }
    }
    return maxValue
}
//既然是26个字母，则考虑直接用Int表示，因为Int有32位，对应的字母存在则位1，否则为0。而判断两数是否相同的字母，只需要两数&一下，如果不为一，则不存在。
fun maxProduct2(words: Array<String>): Int {
    val table = Array(words.size) { 0 }
    for (i in words.indices) {
        val str = words[i]
        str.forEach { char: Char ->
            val index = char - 'a'
            //对应位置设置为1
            table[i] = table[i] or (1 shl index)
        }
    }

    var maxValue = 0
    for (i in words.indices) {
        for (j in (i + 1) until words.size) {
            //只需要两数&一下，如果等于零，则证明两数之前不存在相同的字母
            if (table[i] and table[j] == 0) {
                maxValue = Math.max(maxValue, words[i].length * words[j].length)
            }
        }
    }
    return maxValue
}

/**
 * 只出现一次的数字
 * 给你一个整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。请你找出并返回那个只出现了一次的元素。
 * @param nums 输入的数组
 */
fun singleNumber(nums: IntArray): Int {
    //如果是其余每个元素出现的位数是偶数的话，可以进行便利异或，因为任何数和自己进行异或就是0。
    //但是这次出现的是三次，故不能利用异或。可以将所有数字的二进制形式相加，之后每一位均除以3，如果余数为1，仅出现一次的数字改二进制位数就是1，否则就是0。
    val result = IntArray(32)

    for (i in result.indices) {
        nums.forEach {
            //(it shr (31 - i)) and 1  获取到当前位数
            result[i] += (it shr (31 - i)) and 1
        }
    }

    return result.let { intArray ->
        //二进制数组计算数字的方式
        var r = 0
        for (i in intArray.indices) {
            r += ((intArray[i] % 3) shl (31 - i)).toInt()
        }
        r
    }

}

/**
 * 前n个数字二进制中1的个数   时间复杂度O(nk)
 * 给定一个非负整数 n ，请计算 0 到 n 之间的每个数字的二进制表示中 1 的个数，并输出一个数组。
 * @param n 非负整数n
 */
fun countBitsCase1(n: Int): IntArray {
    //说明: i & (i - 1) 可以将该数字二进制形式中最右侧的1变为0
    //故 可循环处理 i & (i - 1)，即可以计算1的数量  假设一个整数存在k位，则最多有k个1。所以时间复杂度为 O(nk)
    val result = IntArray(n + 1)
    for (i in result.indices) {
        var snapResult = i
        while (snapResult != 0) {
            result[i]++
            snapResult = (snapResult and (snapResult - 1)).toInt()
        }
    }
    return result
}

fun countBitsCase2(n: Int): IntArray {
    //说明: i & (i - 1) 可以将该数字二进制形式中最右侧的1变为0
    //故 而i里面1的个数一定比i & (i - 1)多1。故可以理应result[i] = result[i & (i - 1)] + 1进行计算出结果。最开始的时候result[0] = 0，之后的值都可以从前面的推理出来了。
    //因为需要result[0] = 0，故，循环需要从1开始。
    val result = IntArray(n + 1)
    for (i in 1 until result.size) {
        result[i] = result[(i and (i - 1)).toInt()] + 1
    }
    return result
}

fun countBitsCase3(n: Int): IntArray {
    //如果正整数`i`是偶数，那么它就相当于是将`i/2`左移`1`位的结果；如果正整数`i`是奇数，那么它就相当于是将`i/2`左移`1`位后在`+1`。 `(i shr 1)`用来表示i/2，如果是奇书，则最后一位是1，可以用(i and 1)计算
    val result = IntArray(n + 1)
    for (i in 1 until result.size) {
        result[i] = result[i shr 1] + (i and 1)
    }
    return result
}

/**
 * 二进制加法
 *@param binary1 加数1
 *@param binary2 加数2
 */
fun binaryAdd(binary1: String, binary2: String): String {
    //如果转换成Int什么的类型然后再次进行加法，然后再次转换为二进制，存在溢出的风险。
    //故，仿照十进制加法，右侧对齐，一个一个进行加法。
    var index1 = binary1.length - 1
    var index2 = binary2.length - 1
    val sb = StringBuilder()
    var carry = 0 //进位
    while (index1 >= 0 || index2 >= 0) {
        var char1 = 0
        if (index1 >= 0) {
            char1 = if (binary1[index1--] == '0') 0 else 1
        }
        var char2 = 0
        if (index2 >= 0) {
            char2 = if (binary2[index2--] == '0') 0 else 1
        }
        val sum = char1 + char2 + carry
        if (sum >= 2) {
            carry = 1
            sb.append(sum - 2)
        } else {
            carry = 0
            sb.append(sum)
        }
    }
    //拼接最后的进位
    if (carry > 0) {
        sb.append(carry)
    }
    //需要翻转字符串
    return sb.reverse().toString()
}

/**
 * 基础二进制左移右移测试
 */
fun basicTest() {
    //左移测试   向左边移动，同时右边补0   左移若超出范围，默认向上转型

    //10001010    源码为 11110110   -118
    println(0x8A.toByte())
    //上面左移三位后 01010000    正数源码为自身 01010000
    println(0x50.toByte())
    //shl 左移
    println((-118 shl 3).toByte())
    println((0x8A.toByte().toInt() shl 3).toByte())
    println(0x8A.toByte().toInt() shl 3)

    //右移测试
    // 如果数字是无符号数字，则用0填补最左侧的n位。
    // 如果数字不是无符号数子，若是正数，则左侧（高位）补0，若是负数，则左侧（高位）补1

    //00001010 源码为 00001010
    println(0x0A.toByte())
    println(0x0A.toByte().toInt() shr 2)
    println(0x02.toByte())

    //10001010 源码为 11110110  -118
    println(0x8A.toByte())
    println(0x8A.toByte().toInt() shr 3)
    //右移后 11110001 源码为 10001111  值为 -15
    println(0xF1.toByte())


    //计算整数中1的个数测试
    //i & (i - 1) 可以将该数字二进制形式中最右侧的1变为0
    val num = 8
    println(num and (num - 1))
}




















