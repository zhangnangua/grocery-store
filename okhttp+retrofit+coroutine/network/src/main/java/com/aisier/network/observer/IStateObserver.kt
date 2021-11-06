package com.aisier.network.observer

import androidx.lifecycle.Observer
import com.aisier.network.entity.ApiResponse

abstract class IStateObserver<T> : Observer<ApiResponse<T>> {

    override fun onChanged(apiResponse: ApiResponse<T>) {
        when (apiResponse) {
            is ApiResponse.ApiSuccessResponse -> onSuccess(apiResponse.response)
            is ApiResponse.ApiEmptyResponse -> onDataEmpty()
            is ApiResponse.ApiFailedResponse -> onFailed(
                apiResponse.errorCode,
                apiResponse.errorMsg
            )
            is ApiResponse.ApiErrorResponse -> onError(apiResponse.throwable)
        }
        onComplete()
    }

    abstract fun onSuccess(data: T)

    abstract fun onDataEmpty()

    abstract fun onError(e: Throwable)

    abstract fun onComplete()

    abstract fun onFailed(errorCode: Int?, errorMsg: String?)

}