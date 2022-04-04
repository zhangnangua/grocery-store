package com.zxf.learn_test.LruUtil

import java.util.*

/**
 * LRU(Least Recently Used) 缓存算法，最近最少被使用，则被移除缓存
 *
 * LinkedHashMap，可以保证有序性（访问有序/插入有序）。这里利用到访问有序，即顺序按照访问排列。LinkedHashMap在原来HashMap的Node节点中新增了before和after做成双向链表，用于记录指向顺序
 *
 * @author pumpkin
 */
class LruCache<K, V>(maxSize: Int) {

    /**
     * 当前储存的size
     */
    private var currentSize = 0

    /**
     * 允许保存的最大的size，可以进行重置，不允许小于0
     */
    var maxSize = maxSize
        set(value) {
            if (value < 0) {
                throw IllegalStateException("maxSize 不允许小于0！")
            }
            synchronized(this) {
                field = value
                //重新计算，删除最旧的
                trimToSize()
            }
        }
        get() {
            synchronized(this) {
                return field
            }
        }

    /**
     * 缓存的容量为16，loadFactor为0.75F 与HashMap保持一致
     * 设置accessOrder为true，表示使用访问顺序，即一旦get某个值，该值就会被指向在链表的最后。默认为false，表示的是插入顺序。
     */
    private val cache = LinkedHashMap<K, V>(1 shl 4, 0.75F, true)

    var customSizeOf: ((key: K, value: V) -> Int)? = null

    /**
     * 删除最旧item，直到剩余的item的size小于maxSize
     */
    private fun trimToSize() {
        synchronized(this) {
            if (currentSize < 0 || (cache.isEmpty() && currentSize != 0)) {
                throw IllegalStateException("缓存size记录错误")
            }
            while (currentSize > maxSize) {
                val iterator = cache.entries.iterator()
                if (iterator.hasNext()) {
                    val (key, value) = iterator.next()
                    currentSize -= sizeOf(key, value)
                    iterator.remove()
                }
            }
        }
    }

    /**
     * null 检查
     */
    private fun <T> nullCheck(message: String, any: T?): T {
        if (any == null) {
            throw NullPointerException(message)
        }
        return any
    }

    /**
     * 获取值get
     */
    operator fun get(key: K?): V? {
        nullCheck("key 不允许等于 null", key)
        //利用linkedHashMap的特性，默认会将当前访问的链表指向到最后。
        return cache[key]
    }

    /**
     * 存值put
     * @return 若当前key之前存在值则返回，否则返回null
     */
    operator fun set(key: K?, value: V?): V? {
        nullCheck("key 不允许等于 null", key)
        nullCheck("value 不允许等于 null", value)

        val oldValue = synchronized(this) {
            val size = sizeOf(key!!, value!!)

            //如果存入size 大于maxSize 直接return
            if (size > maxSize) {
                return null
            }

            currentSize += size
            cache.put(key, value)?.also { oldValue ->
                currentSize -= sizeOf(key, oldValue)
            }
        }

        //位置移动
        trimToSize()

        return oldValue
    }


    /**
     * 默认每个item的size返回1，可以修改
     */
    private fun sizeOf(key: K, value: V): Int {
        val snapSizeOf = customSizeOf
        if (snapSizeOf != null) {
            return snapSizeOf(key, value)
        }
        return 1
    }

    /**
     * 清除所有的缓存
     */
    fun clearAll() {
        maxSize = 0
    }

    override fun toString(): String {
        return StringBuilder().apply {
            cache.forEach { (key, value) ->
                append("key is $key ; vlaue is $value")
                append("\n\t")
            }
        }.toString()
    }
}