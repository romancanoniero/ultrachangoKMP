package com.iyr.ultrachango.utils.coroutines

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown Error Occurred")
    }
}

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?= null) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}