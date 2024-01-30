package com.pumpkin.mvvm.util

import android.app.Application
import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.util.SparseArray
import com.pumpkin.data.AppUtil
import com.pumpkin.data.BuildConfig
import com.pumpkin.data.thread.IoScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
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
     * 不挂起  删除最新的值
     */
    private val _events = MutableSharedFlow<Event>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_LATEST
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


abstract class ProcessEventBus<T : Event>(private val eventType: Int, private val context: Application, private val handler: Handler?, pathSegment: String) {

    private val uri: Uri

    private var contentObserver: ContentObserver? = null

    private lateinit var bus: EventBus

    init {
        val packageName: String = context.packageName
        uri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority("$packageName.eventbus")
            .appendEncodedPath(pathSegment)
            .build()
    }

    fun trigger() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "insert()-> ")
        }
        checkInit()
        val contentResolver = context.contentResolver
        contentResolver.notifyChange(uri, null)
    }

    fun getSharedFlow(): Flow<T> {
        checkInit()
        return bus.getSharedFlow()
    }

    private fun checkInit() {
        if (!::bus.isInitialized) {
            synchronized(this) {
                if (!::bus.isInitialized) {
                    bus = EventHelper.getBus(eventType)
                    register()
                }
            }
        }
    }

    private fun register() {
        val local = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) {
                super.onChange(selfChange, uri, flags)
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "onChange()-> selfChange = $selfChange , uri = $uri , flags = $flags")
                }
                IoScope().launch(CoroutineExceptionHandler { coroutineContext, throwable ->
                    if (AppUtil.isDebug) {
                        throwable.printStackTrace()
                    }
                }, CoroutineStart.DEFAULT) {
                    bus.produceEvent(searchData())
                }
            }
        }
        context.contentResolver.registerContentObserver(uri, true, local)
        contentObserver = local
    }

    public fun unregister() {
        val local = contentObserver
        if (local != null) {
            context.contentResolver.unregisterContentObserver(local)
            contentObserver = null
        }
    }

    abstract fun searchData(): T

    companion object {
        const val TAG = "ProcessEventBus"
    }
}

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
