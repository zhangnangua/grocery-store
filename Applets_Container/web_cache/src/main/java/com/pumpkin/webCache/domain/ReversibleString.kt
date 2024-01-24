package com.pumpkin.webCache.domain

/**
 * 一个字符串包装实用程序，可实现高效的字符串反转。 我们经常需要反转字符串。
 * 在 Java 中执行此操作的标准方法是将字符串复制到反向（例如使用 StringBuffer.reverse()）。
 * 当我们只逐个字符地读取字符串时，这似乎很浪费，在这种情况下可以根据需要调换位置。
 */
abstract class ReversibleString private constructor(
    protected val string: String,
    protected val offsetStart: Int,
    protected val offsetEnd: Int
) {

    init {
        if (offsetStart > offsetEnd || offsetStart < 0 || offsetEnd < 0) {
            throw StringIndexOutOfBoundsException("Cannot create negative-length string .")
        }
    }

    abstract val isReversed: Boolean
    abstract fun chatAt(position: Int): Char
    abstract fun substring(startIndex: Int): ReversibleString

    fun length() = offsetEnd - offsetStart

    fun reverse(): ReversibleString {
        return if (isReversed) {
            ForwardString(string, offsetStart, offsetEnd)
        } else {
            ReverseString(string, offsetStart, offsetEnd)
        }
    }

    private class ForwardString(
        string: String,
        offsetStart: Int,
        offsetEnd: Int
    ) : ReversibleString(string, offsetStart, offsetEnd) {
        override val isReversed: Boolean = false

        override fun chatAt(position: Int): Char {
            if (position > length()) {
                throw StringIndexOutOfBoundsException()
            }
            return string[offsetStart + position]
        }

        override fun substring(startIndex: Int): ReversibleString {
            return ForwardString(string, offsetStart + startIndex, offsetEnd)
        }
    }

    private class ReverseString(
        string: String,
        offsetStart: Int,
        offsetEnd: Int
    ) : ReversibleString(string, offsetStart, offsetEnd) {

        override val isReversed: Boolean = true
        override fun chatAt(position: Int): Char {
            if (position > length()) {
                throw StringIndexOutOfBoundsException()
            }
            return string[length() + offsetStart - position - 1]
        }

        override fun substring(startIndex: Int): ReversibleString {
            return ReverseString(string, offsetStart, offsetEnd - startIndex)
        }
    }

    companion object {
        fun create(string: String): ReversibleString {
            return ForwardString(string, 0, string.length)
        }
    }
}



fun String.reversible(): ReversibleString {
    return ReversibleString.create(this)
}

fun String.reverse(): ReversibleString {
    return reversible().reverse()
}