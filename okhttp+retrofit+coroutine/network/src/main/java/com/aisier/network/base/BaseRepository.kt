package com.aisier.network.base

import com.aisier.network.BuildConfig
import com.aisier.network.entity.ApiResponse
import com.aisier.network.entity.handlingApiExceptions
import com.aisier.network.entity.handlingExceptions
import kotlinx.coroutines.delay

open class BaseRepository {

    suspend fun <T> executeHttp(block: suspend () -> ApiResponse<T>): ApiResponse<T> {
        //for test
        delay(500)
        runCatching {
            block.invoke()
        }.onSuccess { data: ApiResponse<T> ->
            return handleHttpOk(data)
        }.onFailure { e ->
            return handleHttpError(e)
        }
        return ApiResponse.ApiEmptyResponse()
    }

    /**
     * 非后台返回错误，捕获到的异常
     */
    private fun <T> handleHttpError(e: Throwable): ApiResponse.ApiErrorResponse<T> {
        if (BuildConfig.DEBUG) e.printStackTrace()
        handlingExceptions(e)
        return ApiResponse.ApiErrorResponse(e)
    }

    /**
     * 返回200，但是还要判断isSuccess
     */
    private fun <T> handleHttpOk(data: ApiResponse<T>): ApiResponse<T> {
        return if (data.isSuccess) {
            getHttpSuccessResponse(data)
        } else {
            handlingApiExceptions(data.errorCode, data.errorMsg)
            ApiResponse.ApiFailedResponse(data.errorCode, data.errorMsg)
        }
    }

    /**
     * 成功和数据为空的处理
     */
    private fun <T> getHttpSuccessResponse(response: ApiResponse<T>): ApiResponse<T> {
        val data = response.data
        return if (data == null || data is List<*> && (data as List<*>).isEmpty()) {
            ApiResponse.ApiEmptyResponse()
        } else {
            ApiResponse.ApiSuccessResponse(data)
        }
    }

}