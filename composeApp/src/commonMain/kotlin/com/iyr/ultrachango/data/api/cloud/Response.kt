package com.iyr.ultrachango.data.api.cloud

import kotlinx.serialization.Serializable

@Serializable
class Response<T>(// Getters and setters
    var code: String? = null, var message: String? = null, var payload: T? = null
)