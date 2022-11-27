package com.pumpkin.okhttp.leetcode.heap

import java.util.PriorityQueue

fun main() {
    val heapTest = HeapTest(3, arrayOf(4, 5, 8, 2))
    println(heapTest.add(3))
    println(heapTest.add(5))
}

class HeapTest(val k: Int, num: Array<Int>) {
    private val minHeap = PriorityQueue<Int>()

    init {
        for (i in num) {
            add(i)
        }
    }

    fun add(e: Int): Int {
        if (minHeap.size < k) {
            minHeap.offer(e)
        } else if (minHeap.peek() < e) {
            minHeap.poll()
            minHeap.offer(e)
        }
        return minHeap.peek()
    }
}