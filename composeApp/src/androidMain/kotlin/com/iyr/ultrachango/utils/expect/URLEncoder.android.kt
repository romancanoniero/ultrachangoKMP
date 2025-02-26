package com.iyr.ultrachango.utils.expect

import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

actual object URLEncoder {
    actual fun encode(input: String): String =
        URLEncoder.encode(input, StandardCharsets.UTF_8.toString())

    actual fun decode(input: String): String =
        URLDecoder.decode(input, StandardCharsets.UTF_8.toString())
}