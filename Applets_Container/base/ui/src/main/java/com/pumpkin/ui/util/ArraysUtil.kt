package com.pumpkin.ui.util

/**
 * 仿照_Arrays库，允许返回非Array本身的类型
 */
inline fun <S, T> Array<out T>.superReduce(operation: (lastValue: S?, T) -> S?): S? {
    if (isEmpty())
        throw UnsupportedOperationException("Empty array can't be reduced.")
    var accumulator: S? = null
    for (index in 0..lastIndex) {
        accumulator = operation(accumulator, this[index])
    }
    return accumulator
}