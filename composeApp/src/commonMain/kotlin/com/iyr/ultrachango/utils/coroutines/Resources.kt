package com.iyr.ultrachango.utils.coroutines

import com.iyr.ultrachango.presentation.auth.AuthErrorType

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown Error Occurred")
    }
}

sealed class Resource<T>(val data: T? = null, val message: String? = null, val exception : Exception? =null, val errorType : AuthErrorType? = null) {
    class Success<T>(data: T?= null) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, exception : Exception? =null,  errorType : AuthErrorType? = null) : Resource<T>(data, message, exception, errorType)
}