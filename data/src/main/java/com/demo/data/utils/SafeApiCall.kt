package com.demo.data.utils

import com.demo.domain.util.Result

suspend fun <T> SafeApiCall(apiCall: suspend () -> T): Result<T> = try {
    Result.Success(apiCall.invoke())
} catch (e: Exception) {
    Result.Error(e)
}