package com.pumpkin.okhttp.leetcode.num

/**
 * pumpkin
 *
 * 整数相关算法题目
 */
fun main() {
    //todo 两数交换  利用异或  不需要进入第三方变量
//    exchangeDoubleNumber(34, 35)

    //利用减法进行除法操作
    //-1090366779
    println(division(-1090366779, 1100540749))
}


/**
 * 题目：输入2个int型整数，它们进行除法计算并返回商，要求不得使用乘号'*'、除号'/'及求余符号'%'。
 * 当发生溢出时，返回最大的整数值。假设除数不为0。例如，输入15和2，输出15/2的结果，即7。
 *
 * @param divisor 除数
 * @param dividend 被除数
 */
fun division(divisor: Int, dividend: Int): Int {
    //Int的范围是负二的三十一次方到二的二的三十一次方减一。  规定正0表示0，负0表示N，其他以绝对值表示。  所以取值范围为N~N-1
    //根据以上的取值范围，所以唯一有可能溢出的是 负二的三十一次方除以负一  此时应该值是二的三十一次方会产生溢出  根据题目的要求返回最大值
    //又因为在java中，二进制已补码的形式存在，正数的补码是其自身，负数的补码为取反加一。 比如 -1 = 100..01  去除符号位取反 111..10 加一 111..11 = 0XFFFFFFFF
    //另在kt中需要调用toInt()，否则默认识别为Long类型   ......坑 需要记录一下
    //此处判断唯一的溢出即为 -2^31 / -1
    if (dividend == 0X80000000.toInt() && divisor == 0XFFFFFFFF.toInt()) {
        return Int.MAX_VALUE
    }

    //考虑到正数转换为负数绝对不会出现越界，所以都转换为负数进行计算
    //符号判断，如果两个除数有一个为负数即值为负数，否则值为正数   所以可以设置 变量为2 判断 如果不存在负数就减一 如果最后等于1 即为负数 否则为正数
    // negative 负
    var snapDivisor = divisor
    var snapDividend = dividend
    var isNegative = 2
    if (divisor > 0) {
        isNegative--
        snapDivisor = -divisor
    }
    if (dividend > 0) {
        isNegative--
        snapDividend = -dividend
    }

    return if (isNegative == 1) {
        -calculateSubtraction(snapDivisor, snapDividend)
    } else {
        calculateSubtraction(snapDivisor, snapDividend)
    }
}

/**
 * 负数循环加法计算 商
 * 核心逻辑，判断每次被除数是不是大于除数的2^k倍  由于每次判断都将除数翻倍，故时间复杂度变为logn
 *
 * 0xC0000000 表示-2^30次方  负数最大值的一般  1100...00  符号位不变补码  1100...00  对应16进制为0xC0000000  为了防止越界
 */
private fun calculateSubtraction(negativeDivisor: Int, negativeDividend: Int): Int {
    var calculateDividend = negativeDividend
    var result = 0
    while (calculateDividend <= negativeDivisor) {
        var snapResult = 1
        var snapFlag = negativeDivisor
        while (snapFlag >= 0xC0000000.toInt() && calculateDividend <= snapFlag + snapFlag) {
            snapResult += snapResult
            snapFlag += snapFlag
        }
        calculateDividend -= snapFlag
        result += snapResult
    }
    return result
}

/**
 * 两数交换，不引入第三个变量
 */
fun exchangeDoubleNumber(num1: Int, num2: Int) {
    var snapNum1 = num1
    var snapNum2 = num2

    val tag = "exchangeDoubleNumber"
    println("$tag 交换前 num1 : $snapNum1 num2 : $snapNum2")

    //两数交换 利用异或 不需要引入第三方变量
    snapNum1 = snapNum1 xor snapNum2
    snapNum2 = snapNum1 xor snapNum2
    snapNum1 = snapNum1 xor snapNum2

    println("$tag 交换后 num1 : $snapNum1 num2 : $snapNum2")
}