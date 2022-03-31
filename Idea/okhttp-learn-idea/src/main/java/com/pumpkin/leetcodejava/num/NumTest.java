package com.pumpkin.leetcodejava.num;


/**
 * pumpkin
 * <p>
 * 整数相关算法题目
 */
public class NumTest {
    public static void main(String[] args) {
//        System.out.println(new NumTest().division(-2147483648, 1));
        System.out.println(0xFFFFFFFF);
    }


    /**
     * 题目：输入2个int型整数，它们进行除法计算并返回商，要求不得使用乘号'*'、除号'/'及求余符号'%'。
     * 当发生溢出时，返回最大的整数值。假设除数不为0。例如，输入15和2，输出15/2的结果，即7。
     *
     * @param divisor  除数
     * @param dividend 被除数
     */
    public int division(int dividend, int divisor) {
        //Int的范围是负二的三十一次方到二的二的三十一次方减一。  规定正0表示0，负0表示N，其他以绝对值表示。  所以取值范围为N~N-1
        //根据以上的取值范围，所以唯一有可能溢出的是 负二的三十一次方除以负一  此时应该值是二的三十一次方会产生溢出  根据题目的要求返回最大值
        //又因为在java中，二进制已补码的形式存在，正数的补码是其自身，负数的补码为取反加一。 比如 -1 = 100..01  去除符号位取反 111..10 加一 111..11 = 0XFFFFFFFF
        //此处判断唯一的溢出即为 -2^31 / -1
        if (dividend == 0x80000000 && divisor == 0xffffffff) {
            return Integer.MAX_VALUE;
        }

        //考虑到正数转换为负数绝对不会出现越界，所以都转换为负数进行计算
        //符号判断，如果两个除数有一个为负数即值为负数，否则值为正数   所以可以设置 变量为2 判断 如果不存在负数就减一 如果最后等于1 即为负数 否则为正数
        // negative 负
        int negative = 2;
        if (dividend < 0) {
            negative--;
        } else {
            dividend = -dividend;
        }
        if (divisor < 0) {
            negative--;
        } else {
            divisor = -divisor;
        }

        int result = calculateSubtraction(dividend, divisor);

        if (negative == 1) {
            return -result;
        } else {
            return result;
        }
    }

    /**
     * 负数循环加法计算 商
     * 核心逻辑，判断每次被除数是不是大于除数的2^k倍  由于每次判断都将除数翻倍，故时间复杂度变为logn
     * <p>
     * 0xC0000000 表示-2^30次方  负数最大值的一般  1100...00  符号位不变补码  1100...00  对应16进制为0xC0000000  为了防止越界
     */
    public int calculateSubtraction(int dividend, int divisor) {
        int result = 0;
        while (dividend <= divisor) {
            int snapDivisor = divisor;
            int snapResult = 1;
            while (snapDivisor >= 0xc0000000 && dividend <= snapDivisor + snapDivisor) {
                snapResult += snapResult;
                snapDivisor += snapDivisor;
            }
            result += snapResult;
            dividend -= snapDivisor;
        }

        return result;
    }
}
