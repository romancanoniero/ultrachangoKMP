package com.iyr.ultrachango.utils.auth_by_cursor.auth



data class NativeAuthResult(
    val user: NativeUser?,
    val error: AuthError?
) {
    companion object {
        fun success(user: NativeUser) = NativeAuthResult(user, null)
        fun error(error: AuthError) = NativeAuthResult(null, error)
        fun error(exception: Exception) = NativeAuthResult(null, AuthError.fromException(exception))
    }
}