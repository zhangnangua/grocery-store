package com.pumpkin.webCache

import android.app.Application
import com.pumpkin.webCache.interceptor.Interceptor
import com.pumpkin.webCache.interceptors.DynamicInterceptor
import com.pumpkin.webCache.requestHelper.DefaultInterceptorConfig
import com.pumpkin.webCache.requestHelper.InterceptorConfig

class WVCacheClient(private val builder: Builder) {

    private var application: Application = builder.application

    private var originUrl: String = builder.originUrl

    private val resourceId: String = builder.resourceId

    private val interceptors: List<Interceptor>?

    val engine: Engine

    init {
        val list = builder.interceptors
        interceptors = if (list != null) {
            ArrayList(list)
        } else {
            null
        }
        engine = Engine(this)
    }

    fun getApplication(): Application {
        return application
    }

    fun getOriginUrl(): String {
        return originUrl
    }

    fun getResourceId(): String {
        return resourceId
    }

    fun getInterceptors(): List<Interceptor>? {
        return interceptors
    }

    class Builder(val application: Application) {
        internal var originUrl = ""
        internal var resourceId = ""
        internal var interceptors: MutableList<Interceptor>? = null
        fun originUrl(originUrl: String): Builder {
            this.originUrl = originUrl
            return this
        }

        fun resourceId(resourceId: String): Builder {
            this.resourceId = resourceId
            return this
        }

        fun dynamicAbility(): Builder {
            dynamicAbility(null)
            return this
        }

        fun dynamicAbility(c: InterceptorConfig?): Builder {
            addInterceptor(DynamicInterceptor(application, c ?: DefaultInterceptorConfig))
            return this
        }

        fun addInterceptor(interceptor: Interceptor): Builder {
            if (interceptors == null) {
                interceptors = ArrayList()
            }
            interceptors!!.add(interceptor)
            return this
        }

        fun build(): WVCacheClient {
            return WVCacheClient(this)
        }
    }
}
