package com.pumpkin.mvvm.util

import android.util.SparseArray
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import java.util.concurrent.locks.ReentrantReadWriteLock

class EventBus {

    /**
     * private mutable shared flow
     *
     * 新订阅默认replay为1
     * 不额外缓存值
     * 挂起直到所有订阅者消费完事件
     */
    private val _events = MutableSharedFlow<Event>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.SUSPEND
    )

    /**
     * publicly exposed as read-only shared flow
     */
    fun <E : Event> getSharedFlow() = _events.asSharedFlow().map {
        it as E
    }

    /**
     * suspends until all subscribers receive it
     */
    suspend fun produceEvent(event: Event) {
        _events.emit(event)
    }
}

interface Event

object EventHelper {
    const val TYPE_RECENTLY = 1
    const val TYPE_HOME_SCROLL_NOTICE = 2

    private val readWriteLock = ReentrantReadWriteLock()
    private val readLock = readWriteLock.readLock()
    private val writeLock = readWriteLock.writeLock()

    private val sets = SparseArray<EventBus>()

    private fun registerBus(type: Int, bus: EventBus) {
        try {
            writeLock.lock()
            sets.append(type, bus)
        } finally {
            writeLock.unlock()
        }
    }

    private fun read(type: Int) = try {
        readLock.lock()
        sets[type]
    } finally {
        readLock.unlock()
    }

    fun getBus(type: Int): EventBus {
        var eventBus = read(type)
        if (eventBus == null) {
            eventBus = EventBus()
            registerBus(type, eventBus)
        }
        return eventBus
    }

}
