package com.iyr.ultrachango.utils.expect

expect object URLEncoder {
    fun encode(input: String): String
    fun decode(input: String): String
}