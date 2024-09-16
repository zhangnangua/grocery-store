package com.pumpkin.okhttp.leetcode.linked_list

fun main() {
    var sum = 1
    while (true) {
        if ((sum shl 1) > 9) {
            println(sum)
            break
        }
        sum = sum shl 1
    }

}